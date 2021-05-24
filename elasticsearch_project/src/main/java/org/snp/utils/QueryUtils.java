package org.snp.utils;

import org.snp.model.credentials.AttributeCredentials;
import org.snp.utils.exception.InternalServerErrorException;


public class QueryUtils {

    public boolean compare(String wordX, String wordY, AttributeCredentials.Comparison comparison){
        switch (comparison){
            case EQ:
                return wordX.equals(wordY);
            case SUP_OR_EQ:
                return Double.valueOf(wordX) >= Double.valueOf(wordY);
            case SUP:
                return Double.valueOf(wordX) > Double.valueOf(wordY);
            case INF_OR_EQ:
                return Double.valueOf(wordX) <= Double.valueOf(wordY);
            case INF:
                return Double.valueOf(wordX) < Double.valueOf(wordY);
            case DIF:
                return ! wordX.equals(wordY);
            default:
                throw new InternalServerErrorException("comparison function does not exist");
        }
    }
}
