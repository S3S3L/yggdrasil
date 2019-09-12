  
package org.s3s3l.yggdrasil.utils.compressor;  
/**
 * <p>
 * </p> 
 * ClassName:DecompressException <br> 
 * Date:     Apr 3, 2019 2:23:32 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class DecompressException extends RuntimeException {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = 78815116297194221L;

    public DecompressException() {
        super();
    }

    public DecompressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DecompressException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecompressException(String message) {
        super(message);
    }

    public DecompressException(Throwable cause) {
        super(cause);
    }

}
  