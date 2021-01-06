package org.s3s3l.yggdrasil.doc.assembler;

import org.s3s3l.yggdrasil.doc.node.DocNode;

public interface DocAssembler {

    String toDocContent(DocNode doc);
}
