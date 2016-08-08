package org.mybatis.scripting.velocity;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.StopCommand;
import org.apache.velocity.runtime.parser.ParserTreeConstants;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.Node;


public class OrderByDirective extends RepeatDirective {

    private static final String VALUES = "ORDER BY";
    private static final String SEPARATOR = ",";

    private String var;

    @Override
    public String getName() {
        return "orderBy";
    }

    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
        super.init(rs, context, node);

        if (node.jjtGetNumChildren() < 2) {
            throw new TemplateInitException("Syntax error", getTemplateName(), getLine(), getColumn());
        }

        Node child = node.jjtGetChild(1);
        if (child.getType() == ParserTreeConstants.JJTREFERENCE) {
            var = ((ASTReference) child).getRootString();
        } else {
            throw new TemplateInitException("Syntax error", getTemplateName(), getLine(), getColumn());
        }
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException,
            ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        Object listObject = node.jjtGetChild(0).value(context);
        if (listObject == null) {
            return false;
        }

        Iterator<?> iterator = null;

        try {
            iterator = rsvc.getUberspect().getIterator(listObject, uberInfo);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Error getting iterator for #in at " + uberInfo;
            rsvc.getLog().error(msg, e);
            throw new VelocityException(msg, e);
        }

        if (iterator == null) {
            throw new VelocityException("Invalid collection");
        }

        Object o = context.get(var);

        ParameterMappingCollector collector = (ParameterMappingCollector) context.get(
                SQLScriptSource.MAPPING_COLLECTOR_KEY);
        String savedItemKey = collector.getItemKey();
        collector.setItemKey(var);
        RepeatScope foreach = new RepeatScope(this, context.get(getName()), var);
        context.put(getName(), foreach);

        if (iterator.hasNext()) {
            writer.append(VALUES);
        }

        NullHolderContext nullHolderContext = null;
        Object value = null;
        while (iterator.hasNext()) {
            value = iterator.next();
            put(context, var, value);
            foreach.index++;
            foreach.hasNext = iterator.hasNext();

            try {
                if (value == null) {
                    if (nullHolderContext == null) {
                        nullHolderContext = new NullHolderContext(var, context);
                    }
                    node.jjtGetChild(node.jjtGetNumChildren() - 1).render(nullHolderContext, writer);
                } else {
                    node.jjtGetChild(node.jjtGetNumChildren() - 1).render(context, writer);
                }
            } catch (StopCommand stop) {
                if (stop.isFor(this)) {
                    break;
                } else {
                    clean(context, o, collector, savedItemKey);
                    throw stop;
                }
            }

            if (iterator.hasNext()) {
                writer.append(SEPARATOR);
            }
        }

        clean(context, o, collector, savedItemKey);
        return true;
    }

}
