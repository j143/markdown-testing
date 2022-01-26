import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Scanner;
import java.util.Set;


public class App {

    public static void doSomething() {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder()
                .nodeRendererFactory(new HtmlNodeRendererFactory() {
                    public NodeRenderer create(HtmlNodeRendererContext context) {
                        return new IndentedCodeBlockNodeRenderer(context);
                    }
                })
                .build();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        Node document = parser.parse(scanner.toString());
        System.out.println(renderer.render(document));

        // "<p>Example:</p>\n<pre>code\n</pre>\n"
    }
}

class IndentedCodeBlockNodeRenderer implements NodeRenderer {

    private final HtmlWriter html;

    IndentedCodeBlockNodeRenderer(HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }

    public Set<Class<? extends Node>> getNodeTypes() {
        // Return the node types we want to use this renderer for.
        return Collections.<Class<? extends Node>>singleton(IndentedCodeBlock.class);
    }

    public void render(Node node) {
        // We only handle one type as per getNodeTypes, so we can just cast it here.
        IndentedCodeBlock codeBlock = (IndentedCodeBlock) node;
        html.line();
        html.tag("pre");
        html.text(codeBlock.getLiteral());
        html.tag("/pre");
        html.line();
    }
}