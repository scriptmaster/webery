package com.wizzardo.http.framework.template.taglib.g;

import com.wizzardo.http.framework.template.Body;
import com.wizzardo.http.framework.template.ExpressionHolder;
import com.wizzardo.http.framework.template.RenderResult;
import com.wizzardo.http.framework.template.Tag;
import com.wizzardo.tools.collections.CollectionTools;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wizzardo on 12.05.15.
 */
public class Join extends Tag {
    public Tag init(Map<String, String> attrs, Body body, String offset) {
        ExpressionHolder<Collection> in = asExpression(attrs, "in", false, true);
        String delimiter = attrs.getOrDefault("delimiter", ", ");

        add((model, result) -> {
            Collection src = in.getRaw(model);
            result.append(CollectionTools.join(src, delimiter));
            return result;
        });
        append("\n");
        return this;
    }
}
