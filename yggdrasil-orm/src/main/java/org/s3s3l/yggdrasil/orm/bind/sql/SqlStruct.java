package org.s3s3l.yggdrasil.orm.bind.sql;

import java.util.List;

public interface SqlStruct {

    String getSql();

    List<Object> getParams();

}
