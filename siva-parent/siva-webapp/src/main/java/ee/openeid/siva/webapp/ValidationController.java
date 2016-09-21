package ee.openeid.siva.webapp;

import ee.openeid.siva.proxy.ValidationProxy;
import ee.openeid.siva.validation.document.report.QualifiedReport;
import ee.openeid.siva.webapp.request.JSONValidationRequest;
import ee.openeid.siva.webapp.response.erroneus.RequestValidationError;
import ee.openeid.siva.webapp.transformer.ValidationRequestToProxyDocumentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ValidationController {

    private ValidationProxy validationProxy;
    private ValidationRequestToProxyDocumentTransformer transformer;

    @RequestMapping(value = "/validate", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public QualifiedReport validate(@Valid @RequestBody JSONValidationRequest validationRequest) {
        return validationProxy.validate(transformer.transform(validationRequest));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RequestValidationError invalidRequest(MethodArgumentNotValidException exception) {
        RequestValidationError requestValidationError = new RequestValidationError();
        BindingResult bindingResult = exception.getBindingResult();
        bindingResult.getFieldErrors().forEach(br -> requestValidationError.addFieldError(br.getField(), br.getDefaultMessage()));
        return requestValidationError;
    }

    @Autowired
    public void setValidationProxy(ValidationProxy validationProxy) {
        this.validationProxy = validationProxy;
    }

    @Autowired
    public void setTransformer(ValidationRequestToProxyDocumentTransformer transformer) {
        this.transformer = transformer;
    }

}