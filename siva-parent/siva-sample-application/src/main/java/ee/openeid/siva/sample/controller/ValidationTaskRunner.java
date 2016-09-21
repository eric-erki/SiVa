/*
 * Copyright 2016 Riigi Infosüsteemide Amet
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */

package ee.openeid.siva.sample.controller;

import ee.openeid.siva.sample.cache.UploadedFile;
import ee.openeid.siva.sample.siva.SivaServiceType;
import ee.openeid.siva.sample.siva.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ee.openeid.siva.sample.controller.ValidationResultType.JSON;
import static ee.openeid.siva.sample.controller.ValidationResultType.SOAP;

@Service
class ValidationTaskRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationTaskRunner.class);

    private ValidationService jsonValidationService;
    private ValidationService soapValidationService;

    private final Map<ValidationResultType, String> validationResults = new ConcurrentHashMap<>();

    void run(UploadedFile uploadedFile) throws InterruptedException {
        Map<ValidationResultType, ValidationService> serviceMap = getValidationServiceMap();

        ExecutorService executorService = Executors.newFixedThreadPool(serviceMap.size());
        serviceMap.entrySet().forEach(entry -> {
            executorService.submit(() -> validateFile(entry.getValue(), entry.getKey(), uploadedFile));
        });

        executorService.shutdown();
        executorService.awaitTermination(2, TimeUnit.MINUTES);
    }

    private Map<ValidationResultType, ValidationService> getValidationServiceMap() {
        return Stream.of(addEntry(JSON, jsonValidationService), addEntry(SOAP, soapValidationService))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static SimpleImmutableEntry<ValidationResultType, ValidationService> addEntry(
            ValidationResultType json,
            ValidationService jsonValidationService
    ) {
        return new SimpleImmutableEntry<>(json, jsonValidationService);
    }

    private void validateFile(
            ValidationService validationService,
            ValidationResultType resultType,
            UploadedFile uploadedFile
    ) {
        try {
            String validationResult = validationService.validateDocument(uploadedFile)
                    .toBlocking()
                    .first();

            validationResults.put(resultType, validationResult);
        } catch (IOException e) {
            LOGGER.warn("Uploaded file validation failed with error: {}", e.getMessage(), e);
        }
    }

    String getValidationResult(ValidationResultType resultType) {
        return validationResults.get(resultType);
    }

    void clearValidationResults() {
        validationResults.clear();
    }

    @Autowired
    @Qualifier(value = SivaServiceType.JSON_SERVICE)
    public void setJsonValidationService(final ValidationService jsonValidationService) {
        this.jsonValidationService = jsonValidationService;
    }

    @Autowired
    @Qualifier(value = SivaServiceType.SOAP_SERVICE)
    public void setSoapValidationService(ValidationService soapValidationService) {
        this.soapValidationService = soapValidationService;
    }
}