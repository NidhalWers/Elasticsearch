package org.snp.utils;

import org.snp.model.credentials.AttributeCredentials;
import org.snp.utils.exception.InternalServerErrorException;


public class ComparisonUtils {

    public static boolean compare(String wordX, String wordY, AttributeCredentials.Operator operator){
        switch (operator){
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
