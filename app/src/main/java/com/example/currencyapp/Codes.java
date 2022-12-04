package com.example.currencyapp;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Codes {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("documentation")
    @Expose
    private String documentation;
    @SerializedName("terms_of_use")
    @Expose
    private String termsOfUse;
    @SerializedName("supported_codes")
    @Expose
    private List<List<String>> supportedCodes = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getTermsOfUse() {
        return termsOfUse;
    }

    public void setTermsOfUse(String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    public List<List<String>> getSupportedCodes() {
        return supportedCodes;
    }

    public void setSupportedCodes(List<List<String>> supportedCodes) {
        this.supportedCodes = supportedCodes;
    }

}