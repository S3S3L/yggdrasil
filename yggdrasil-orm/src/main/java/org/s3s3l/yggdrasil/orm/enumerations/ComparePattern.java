package org.s3s3l.yggdrasil.orm.enumerations;

/**
 * 
 * <p>
 * </p>
 * ClassName: ComparePattern <br>
 * date: Sep 20, 2019 11:30:08 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public enum ComparePattern {
    EQUAL("="),
    UNEQUAL("<>"),
    LAGER(">"),
    LESS("<"),
    NOT_LAGER("<="),
    NOT_LESS(">="),
    NULL("IS NOT NULL"),
    NON_NULL("IS NULL"),
    START_WITH("LIKE"),
    END_WITH("LIKE"),
    LIKE("LIKE"),
    
    IN("IN");

    private String operator;

    private ComparePattern(String operator) {
        this.operator = operator;
    }

    public String operator() {
        return this.operator;
    }
}
