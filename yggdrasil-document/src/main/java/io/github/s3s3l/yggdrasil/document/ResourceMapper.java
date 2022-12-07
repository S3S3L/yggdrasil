package io.github.s3s3l.yggdrasil.document;

import java.util.List;

/**
 * <p>
 * </p> 
 * ClassName:ResourceMapper <br> 
 * Date:     Dec 27, 2017 2:51:37 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface ResourceMapper {
    <T> List<T> mapList(int colunmIndex, Class<T> type);

    <T> List<T> mapList(int colunmIndex, int offsetRow, Class<T> type);
}
  