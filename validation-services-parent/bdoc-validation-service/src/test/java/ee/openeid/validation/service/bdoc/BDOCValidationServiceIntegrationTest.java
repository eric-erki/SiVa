package ee.openeid.validation.service.bdoc;

import ee.openeid.siva.validation.document.ValidationDocument;
import ee.openeid.siva.validation.document.report.Error;
import ee.openeid.siva.validation.document.report.QualifiedReport;
import ee.openeid.siva.validation.document.report.SignatureScope;
import ee.openeid.siva.validation.document.report.SignatureValidationData;
import ee.openeid.siva.validation.exception.MalformedDocumentException;
import ee.openeid.tsl.TSLLoader;
import ee.openeid.tsl.configuration.TSLLoaderConfiguration;
import ee.openeid.validation.service.bdoc.configuration.BDOCValidationServiceConfiguration;
import org.apache.commons.lang.StringUtils;
import org.digidoc4j.Configuration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    TSLLoaderConfiguration.class,
    TSLLoader.class,
    BDOCValidationServiceConfiguration.class,
    BDOCValidationService.class
})
@ActiveProfiles("test")
public class BDOCValidationServiceIntegrationTest {
    
    @Autowired
    private BDOCValidationService bdocValidationService;

    @Autowired
    private Configuration configuration;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void verifyCorrectPolicyIsLoadedToD4JConfiguration() throws Exception {
        assertTrue(configuration.getValidationPolicy().contains("EE_constraint"));
    }

    @Test
    public void validatingABDOCWithMalformedBytesResultsInMalformedDocumentException() throws Exception {
        ValidationDocument validationDocument = new ValidationDocument();
        validationDocument.setBytes("Hello".getBytes());
        expectedException.expect(MalformedDocumentException.class);
        expectedException.expectMessage("the document is malformed");
        bdocValidationService.validateDocument(validationDocument);
    }

    @Test
    public void bdocValidationResultShouldIncludeQualifiedReportPOJO() throws Exception {
        QualifiedReport validationResult2Signatures = bdocValidationService.validateDocument(bdocValid2Signatures());
        assertNotNull(validationResult2Signatures);
    }

    @Test
    public void qualifiedReportShouldIncludeRequiredFields() throws Exception {
        QualifiedReport validationResult2Signatures = bdocValidationService.validateDocument(bdocValid2Signatures());
        assertNotNull(validationResult2Signatures.getPolicy());
        assertNotNull(validationResult2Signatures.getValidationTime());
        assertEquals(BDOCTestUtils.VALID_BDOC_TM_2_SIGNATURES, validationResult2Signatures.getDocumentName());
        assertTrue(validationResult2Signatures.getSignatures().size() == 2);
        assertTrue(validationResult2Signatures.getValidSignaturesCount() == 2);
        assertTrue(validationResult2Signatures.getSignaturesCount() == 2);
    }

    @Test
    public void qualifiedReportShouldHaveCorrectSignatureValidationDataForSignature1() throws Exception {
        QualifiedReport validationResult2Signatures = bdocValidationService.validateDocument(bdocValid2Signatures());
        SignatureValidationData sig1 = validationResult2Signatures.getSignatures()
                .stream()
                .filter(sig -> sig.getId().equals("S0"))
                .findFirst()
                .get();

        assertEquals("XAdES_BASELINE_LT_TM", sig1.getSignatureFormat());
        assertEquals("QES", sig1.getSignatureLevel());
        assertEquals("JUHANSON,ALLAN,38608014910", sig1.getSignedBy());
        assertEquals(SignatureValidationData.Indication.TOTAL_PASSED.toString(), sig1.getIndication());
        assertTrue(StringUtils.isEmpty(sig1.getSubIndication()));
        assertTrue(sig1.getErrors().size() == 0);
        assertTrue(sig1.getWarnings().size() == 0);
        assertTrue(sig1.getSignatureScopes().size() == 1);
        SignatureScope scope = sig1.getSignatureScopes().get(0);
        assertEquals("chrome-signing.log", scope.getName());
        assertEquals("Full document", scope.getContent());
        assertEquals("FullSignatureScope", scope.getScope());
        assertEquals("2016-05-04T08:43:55Z", sig1.getClaimedSigningTime());
        assertEquals("2016-05-04T08:44:23Z", sig1.getInfo().getBestSignatureTime());
    }

    @Test
    public void qualifiedReportShouldHaveCorrectSignatureValidationDataForSignature2() throws Exception {
        QualifiedReport validationResult2Signatures = bdocValidationService.validateDocument(bdocValid2Signatures());
        SignatureValidationData sig2 = validationResult2Signatures.getSignatures()
                .stream()
                .filter(sig -> sig.getId().equals("S1"))
                .findFirst()
                .get();

        assertEquals("XAdES_BASELINE_LT_TM", sig2.getSignatureFormat());
        assertEquals("QES", sig2.getSignatureLevel());
        assertEquals("VOLL,ANDRES,39004170346", sig2.getSignedBy());
        assertEquals(SignatureValidationData.Indication.TOTAL_PASSED.toString(), sig2.getIndication());
        assertTrue(StringUtils.isEmpty(sig2.getSubIndication()));
        assertTrue(sig2.getErrors().size() == 0);
        assertTrue(sig2.getWarnings().size() == 0);
        assertTrue(sig2.getSignatureScopes().size() == 1);
        SignatureScope scope = sig2.getSignatureScopes().get(0);
        assertEquals("chrome-signing.log", scope.getName());
        assertEquals("Full document", scope.getContent());
        assertEquals("FullSignatureScope", scope.getScope());
        assertEquals("2016-05-04T11:14:23Z", sig2.getClaimedSigningTime());
        assertEquals("2016-05-04T11:14:32Z", sig2.getInfo().getBestSignatureTime());
    }

    @Test
    public void reportForBdocTSWithUntrustedRevocationDataShouldContainError() throws Exception {
        QualifiedReport validationResultSignedNoManifest = bdocValidationService.validateDocument(bdocTSIndeterminateNoManifest());
        assertTrue(validationResultSignedNoManifest.getValidSignaturesCount() == 0);
        SignatureValidationData sig = validationResultSignedNoManifest.getSignatures().get(0);
        assertTrue(sig.getErrors().size() == 1);

        Error error = sig.getErrors().get(0);
        assertEquals("BBB_XCV_IRDTFC_ANS", error.getNameId());
        assertEquals("The revocation data for the certificate is not trusted!", error.getContent());
        assertEquals(sig.getIndication(), SignatureValidationData.Indication.INDETERMINATE.toString());
    }

    private ValidationDocument bdocValid2Signatures() throws Exception {
        return BDOCTestUtils.buildValidationDocument(BDOCTestUtils.VALID_BDOC_TM_2_SIGNATURES);
    }

    private ValidationDocument bdocTSIndeterminateNoManifest() throws Exception {
        return BDOCTestUtils.buildValidationDocument(BDOCTestUtils.TS_NO_MANIFEST);
    }
}