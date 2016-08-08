package org.mybatis.scripting.velocity;

import java.io.IOException;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.mybatis.scripting.velocity.WhereDirective;

public class WhereExtDirective extends WhereDirective {

    @Override
    public String getName() {
        return "whereExt";
    }

    @Override
    protected Params getParams(InternalContextAdapter context, Node node)
            throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        Params params = super.getParams(context, node);
        if (params.getBody().isEmpty()) {
            return null;
        }
        return params;
    }
}

