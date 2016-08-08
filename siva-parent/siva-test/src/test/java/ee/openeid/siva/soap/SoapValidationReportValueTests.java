package ee.openeid.siva.soap;

import ee.openeid.siva.integrationtest.configuration.IntegrationTest;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Document;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTest.class)
public class SoapValidationReportValueTests extends SiVaSoapTests {
    @Before
    public void DirectoryBackToDefault() {
        setTestFilesDirectory(DEFAULT_TEST_FILES_DIRECTORY);
    }

    private static final String DEFAULT_TEST_FILES_DIRECTORY = "bdoc/live/timestamp/";

    private String testFilesDirectory = DEFAULT_TEST_FILES_DIRECTORY;

    public void setTestFilesDirectory(String testFilesDirectory) {
        this.testFilesDirectory = testFilesDirectory;
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-1
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report XAdES_BASELINE_LT_TM, QES, FullSignatureScope
     *
     * Expected Result: All required elements are present and meet the expected values.
     *
     * File: Valid_ID_sig.bdoc
     *
     ***/
    @Test
    public void SoapBdocCorrectValuesArePresentValidLtTmSignature() {
        setTestFilesDirectory("bdoc/live/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("Valid_ID_sig.bdoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getValidSignaturesCount(),getQualifiedReportFromDom(report).getSignaturesCount());
        assertEquals("XAdES_BASELINE_LT_TM", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSubIndication().isEmpty());
        assertEquals("QES", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("FullSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-2
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report XAdES_BASELINE_LT, QES, FullSignatureScope
     *
     * Expected Result: All required elements are present and meet the expected values.
     *
     * File: 23635_bdoc_ts_OCSP_random_nonce.bdoc
     *
     ***/
    @Test
    public void SoapBdocCorrectValuesArePresentValidLtSignature() {
        setTestFilesDirectory("bdoc/live/timestamp/");
        Document report = extractReportDom(post(validationRequestForDocument("23635_bdoc_ts_OCSP_random_nonce.bdoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getValidSignaturesCount(),getQualifiedReportFromDom(report).getSignaturesCount());
        assertEquals("XAdES_BASELINE_LT", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSubIndication().isEmpty());
        assertEquals("QES", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("FullSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-3
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report XAdES_BASELINE_LT, AdES, FullSignatureScope
     *
     * Expected Result: All required elements are present and meet the expected values.
     *
     * File: 23154_test1-old-sig-sigat-NOK-prodat-OK-1.bdoc
     *
     ***/
    @Test
    public void SoapBdocCorrectValuesArePresentValidLtSignatureAdes() {
        setTestFilesDirectory("bdoc/test/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("23154_test1-old-sig-sigat-NOK-prodat-OK-1.bdoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getValidSignaturesCount(),getQualifiedReportFromDom(report).getSignaturesCount());
        assertEquals("XAdES_BASELINE_LT_TM", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSubIndication().isEmpty());
        assertEquals("AdES", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("FullSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertEquals("The certificate is not supported by SSCD!", getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().get(0).getDescription());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-4
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report XAdES_BASELINE_LT_TM, AdESqc, FullSignatureScope
     *
     * Expected Result: All required elements are present and meet the expected values.
     *
     * File: 23200_weakdigest-wrong-nonce.asice
     *
     ***/
    @Test @Ignore //TODO: VAL-242 Subindication is empty although in case of failure it is expected to have value
    public void SoapBdocCorrectValuesArePresentValidLtSignatureAdesqc() {
        setTestFilesDirectory("bdoc/test/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("23200_weakdigest-wrong-nonce.asice")).andReturn().body().asString());
        assertTrue(getQualifiedReportFromDom(report).getValidSignaturesCount()== 0);
        assertEquals("XAdES_BASELINE_LT_TM", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-FAILED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertEquals("ReplaceWithCorrectValue", getQualifiedReportFromDom(report).getSignatures().get(0).getSubIndication());
        assertEquals("AdESqc", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("FullSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-5
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report XAdES_BASELINE_LTA, QES, FullSignatureScope
     *
     * Expected Result: All required elements are present and meet the expected values.
     *
     * File: EE_SER-AEX-B-LTA-V-24.pdf
     *
     ***/
    @Test
    public void SoapBdocCorrectValuesArePresentInvalidLtSignatureAdesqc() {
        setTestFilesDirectory("bdoc/live/timestamp/");
        Document report = extractReportDom(post(validationRequestForDocument("EE_SER-AEX-B-LTA-V-24.bdoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getSignaturesCount(), getQualifiedReportFromDom(report).getValidSignaturesCount());
        assertEquals("XAdES_BASELINE_LTA", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertEquals("QES",getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("FullSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-6
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report, xml v1.0, checks for missing info
     *
     * Expected Result: All required elements are present and meet the expected values and other values are empty as expected.
     *
     * File: SK-XML1.0.ddoc
     *
     ***/
    @Test @Ignore //TODO: VAL-238 Travis fails the test, although in local machine it passes
    public void SoapDdocCorrectValuesArePresentV1_0() {
        setTestFilesDirectory("ddoc/live/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("SK-XML1.0.ddoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getSignaturesCount(),getQualifiedReportFromDom(report).getValidSignaturesCount());
        assertEquals("SK_XML_1.0", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel().isEmpty());
        assertEquals("", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-7
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report, xml v1.1, checks for missing info
     *
     * Expected Result: All required elements are present and meet the expected values and other values are empty as expected.
     *
     * File: Igasugust1.1.ddoc
     *
     ***/
    @Test
    public void SoapDdocCorrectValuesArePresentV1_1() {
        setTestFilesDirectory("ddoc/live/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("igasugust1.1.ddoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getSignaturesCount(),getQualifiedReportFromDom(report).getValidSignaturesCount());
        assertEquals("DIGIDOC_XML_1.1", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel().isEmpty());
        assertEquals("", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-8
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report, xml v1.2, checks for missing info
     *
     * Expected Result: All required elements are present and meet the expected values and other values are empty as expected.
     *
     * File: Igasugust1.2.ddoc
     *
     ***/
    @Test
    public void SoapDdocCorrectValuesArePresentV1_2() {
        setTestFilesDirectory("ddoc/live/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("igasugust1.2.ddoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getSignaturesCount(),getQualifiedReportFromDom(report).getValidSignaturesCount());
        assertEquals("DIGIDOC_XML_1.2", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel().isEmpty());
        assertEquals("", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-9
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report, xml v1.3, checks for missing info
     *
     * Expected Result: All required elements are present and meet the expected values and other values are empty as expected.
     *
     * File: Igasugust1.3.ddoc
     *
     ***/
    @Test
    public void SoapDdocCorrectValuesArePresentV1_3() {
        setTestFilesDirectory("ddoc/live/timemark/");
        Document report = extractReportDom(post(validationRequestForDocument("igasugust1.3.ddoc")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getSignaturesCount(),getQualifiedReportFromDom(report).getValidSignaturesCount());
        assertEquals("DIGIDOC_XML_1.3", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel().isEmpty());
        assertEquals("", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-10
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report, PAdES_baseline_LT, checks for missing info
     *
     * Expected Result: All required elements are present and meet the expected values and other values are empty as expected.
     *
     * File: pades_lt_two_valid_sig.pdf
     *
     ***/
    @Test
    public void SoapPadesCorrectValuesArePresentBaselineLtSignature() {
        setTestFilesDirectory("pdf/baseline_profile_test_files/");
        Document report = extractReportDom(post(validationRequestForDocument("pades_lt_two_valid_sig.pdf")).andReturn().body().asString());
        assertEquals(getQualifiedReportFromDom(report).getSignaturesCount(),getQualifiedReportFromDom(report).getValidSignaturesCount());
        assertEquals("PAdES_BASELINE_LT", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-PASSED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertEquals("QES", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("PdfByteRangeSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getErrors().isEmpty());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    /***
     *
     * TestCaseID: Soap-ValidationReportValue-11
     *
     * TestType: Automated
     *
     * RequirementID: http://open-eid.github.io/SiVa/siva/interface_description/
     *
     * Title: Verification of values in Validation Report, PAdES_baseline_B, checks for missing info
     *
     * Expected Result: All required elements are present and meet the expected values and other values are empty as expected.
     *
     * File: hellopades-pades-b-sha256-auth.pdf
     *
     ***/
    @Test @Ignore //TODO: VAL-242
    public void SoapPadesCorrectValuesArePresentInvalidBaselineBSignature() {
        setTestFilesDirectory("pdf/baseline_profile_test_files/");
        Document report = extractReportDom(post(validationRequestForDocument("hellopades-pades-b-sha256-auth.pdf")).andReturn().body().asString());
        assertTrue(getQualifiedReportFromDom(report).getValidSignaturesCount()==0);
        assertEquals("PAdES_BASELINE_B", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureFormat());
        assertEquals("TOTAL-FAILED", getQualifiedReportFromDom(report).getSignatures().get(0).getIndication());
        assertEquals("ReplaceWithCorrectValue", getQualifiedReportFromDom(report).getSignatures().get(0).getSubIndication());
        assertEquals("QES", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureLevel());
        assertEquals("FullSignatureScope", getQualifiedReportFromDom(report).getSignatures().get(0).getSignatureScopes().get(0).getScope());
        assertTrue(getQualifiedReportFromDom(report).getSignatures().get(0).getWarnings().isEmpty());
    }

    @Override
    protected String getTestFilesDirectory() {
        return testFilesDirectory;
    }
}