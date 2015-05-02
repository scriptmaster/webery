package com.wizzardo.http.framework;

import com.wizzardo.epoll.readable.ReadableBuilder;
import com.wizzardo.http.Handler;
import com.wizzardo.http.framework.di.DependencyFactory;
import com.wizzardo.http.framework.template.RenderResult;
import com.wizzardo.http.framework.template.Renderer;
import com.wizzardo.http.request.Request;
import com.wizzardo.http.response.Response;
import com.wizzardo.tools.misc.Unchecked;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by wizzardo on 02.05.15.
 */
public class ControllerHandler implements Handler {

    protected Class<? extends Controller> controller;
    protected Method action;

    public ControllerHandler(Class<? extends Controller> controller, String action) {
        this.controller = controller;
        this.action = findAction(controller, action);
    }


    @Override
    public Response handle(Request request, Response response) throws IOException {
//        request.controller(controllerName);
//        request.action(actionName);

        Controller c = DependencyFactory.getDependency(controller);
        c.request = request;
        c.response = response;

        RenderResult render = Unchecked.call(() -> (Renderer) action.invoke(c)).render();

        ReadableBuilder builder = new ReadableBuilder();
        render.provideBytes(builder::append);
        response.setBody(builder);

        return response;
    }

    protected Method findAction(Class clazz, String action) {
        for (Method method : clazz.getDeclaredMethods()) {
            if ((method.getModifiers() & Modifier.STATIC) == 0 && method.getName().equals(action)) {
                method.setAccessible(true);
                return method;
            }
        }
        throw new IllegalStateException("Can't find action '" + action + "'");
    }
}
