package org.jboss.as.console.client.rbac;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.jboss.ballroom.client.rbac.SecurityContext;

/**
 * @author Heiko Braun
 * @date 8/7/13
 */
public class RBACUtil {


    public static SafeHtml dump(SecurityContext sc) {
        SafeHtmlBuilder html = new SafeHtmlBuilder();

        SecurityContextImpl context = (SecurityContextImpl)sc;

        html.appendHtmlConstant("<h2>Required Resources for: "+context.nameToken+"</h2>");
        html.appendHtmlConstant("<ul>");
        for(String requiredResource : context.requiredResources)
        {
            html.appendHtmlConstant("<li>").appendEscaped(requiredResource).appendHtmlConstant("</li>");
        }
        html.appendHtmlConstant("</ul><p/>");

        html.appendHtmlConstant("<h2>Constraints</h2>");

        for(String resource : context.accessConstraints.keySet())
        {
            html.appendHtmlConstant("<h3>").appendEscaped(resource).appendHtmlConstant("</h3>");

            Constraints constraints = context.accessConstraints.get(resource);
            html.appendHtmlConstant("<ul>");
            html.appendHtmlConstant("<li>").appendEscaped("read-config:"+constraints.isReadConfig()).appendHtmlConstant("</li>");
            html.appendHtmlConstant("<li>").appendEscaped("write-config:"+constraints.isWriteConfig()).appendHtmlConstant("</li>");
            html.appendHtmlConstant("<li>").appendEscaped("read-runtime:"+constraints.isReadRuntime()).appendHtmlConstant("</li>");
            html.appendHtmlConstant("<li>").appendEscaped("write-runtime:"+constraints.isWriteRuntime()).appendHtmlConstant("</li>");
            html.appendHtmlConstant("</ul>");


            html.appendHtmlConstant("<p/>");

            html.appendHtmlConstant("<h4>Attributes</h4>");

            html.appendHtmlConstant("<table border='0' cellpadding='5'>");
            html.appendHtmlConstant("<tr>");
            html.appendHtmlConstant("<th>");
            html.appendEscaped("Attribute Name");
            html.appendHtmlConstant("</th>");
            html.appendHtmlConstant("<th>");
            html.appendEscaped("Read");
            html.appendHtmlConstant("</th>");
            html.appendHtmlConstant("<th>");
            html.appendEscaped("Write");
            html.appendHtmlConstant("</th>");
            html.appendHtmlConstant("</tr>");

            for(String att : constraints.attributePermissions.keySet())
            {
                html.appendHtmlConstant("<tr>");
                html.appendHtmlConstant("<td>");
                html.appendEscaped(att);
                html.appendHtmlConstant("</td>");
                html.appendHtmlConstant("<td>");
                Constraints.AttributePerm attributePerm = constraints.attributePermissions.get(att);
                html.appendEscaped(String.valueOf(attributePerm.isRead()));
                html.appendHtmlConstant("</td>");
                html.appendHtmlConstant("<td>");
                html.appendEscaped(String.valueOf(attributePerm.isWrite()));
                html.appendHtmlConstant("</td>");
                html.appendHtmlConstant("</tr>");
            }
            html.appendHtmlConstant("</table>");

            html.appendHtmlConstant("<p/>");

            html.appendHtmlConstant("<h4>Operations</h4>");

            html.appendHtmlConstant("<table border='0' cellpadding='5'>");
            html.appendHtmlConstant("<tr>");
            html.appendHtmlConstant("<th>");
            html.appendEscaped("Operation Name");
            html.appendHtmlConstant("</th>");
            html.appendHtmlConstant("<th>");
            html.appendEscaped("Exec");
            html.appendHtmlConstant("</th>");
            html.appendHtmlConstant("</tr>");

            if(!constraints.execPermission.isEmpty())
            {
                for(String op : constraints.execPermission.get(resource))
                {
                    html.appendHtmlConstant("<tr>");
                    html.appendHtmlConstant("<td>");
                    html.appendEscaped(op);
                    html.appendHtmlConstant("</td>");
                    html.appendHtmlConstant("<td>");
                    html.appendEscaped(String.valueOf(constraints.isOperationExec(resource, op)));
                    html.appendHtmlConstant("</td>");
                    html.appendHtmlConstant("</tr>");
                }
            }

            html.appendHtmlConstant("</table>");
        }


        return html.toSafeHtml();
    }
}

