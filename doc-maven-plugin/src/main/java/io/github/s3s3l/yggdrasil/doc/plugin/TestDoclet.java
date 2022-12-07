package io.github.s3s3l.yggdrasil.doc.plugin;

import java.io.PrintStream;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner9;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.util.DocTreeScanner;
import com.sun.source.util.DocTrees;

import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.StandardDoclet;

/**
 * Just a doclet test.
 * 
 * @version 1.0
 * @author Zwei
 */
public class TestDoclet extends StandardDoclet {
    private DocTrees treeUtils;

    /**
     * @name testName
     * @param docEnv
     *                   env of javadoc
     */
    @Override
    public boolean run(DocletEnvironment docEnv) {
        System.out.println("hello doclet");
        treeUtils = docEnv.getDocTrees();
        docEnv.getSpecifiedElements()
                .forEach(System.out::println);
        ShowElements se = new ShowElements(System.out);
        se.show(docEnv.getIncludedElements());
        return true;
    }

    class ShowElements extends ElementScanner9<Void, Integer> {
        final PrintStream out;

        ShowElements(PrintStream out) {
            this.out = out;
        }

        void show(Set<? extends Element> elements) {
            scan(elements, 0);
        }

        @Override
        public Void scan(Element e, Integer depth) {
            DocCommentTree dcTree = treeUtils.getDocCommentTree(e);
            if (dcTree != null) {
                out.println("==============");
                new ShowDocTrees(out).scan(dcTree, depth + 1);
            }
            System.out.println("ElementName: " + e.toString());
            System.out.println("ElementKind: " + e.getKind());
            System.out.println("ElementType: " + e.getClass());
            if (e instanceof TypeElement) {
                Name qualifiedName = ((TypeElement) e).getQualifiedName();
                System.out.println("[Type] " + qualifiedName);
            }
            return super.scan(e, depth + 1);
        }
    }

    class ShowDocTrees extends DocTreeScanner<Void, Integer> {
        final PrintStream out;

        ShowDocTrees(PrintStream out) {
            this.out = out;
        }

        @Override
        public Void scan(DocTree t, Integer depth) {
            out.println("docTreeKind: " + t.getKind());
            out.println("docTreeType: " + t.getClass());
            out.println("deep: " + depth);
            out.println(t.toString());
            out.println();
            return super.scan(t, depth + 1);
        }
    }

}
