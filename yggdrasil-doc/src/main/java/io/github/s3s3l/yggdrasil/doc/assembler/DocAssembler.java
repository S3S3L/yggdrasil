package io.github.s3s3l.yggdrasil.doc.assembler;

import io.github.s3s3l.yggdrasil.doc.node.DocNode;

public interface DocAssembler {

    String toDocContent(DocNode doc);
}
