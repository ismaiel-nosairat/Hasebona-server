/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.configurations;

import io.ismaiel.hasebona.entities.Permission;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.models.FinalResponse;
import io.ismaiel.hasebona.models.Session;
import io.ismaiel.hasebona.repos.PermissionRepo;
import io.ismaiel.hasebona.services.Core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Component
public class PermissionFilter implements Filter {

    @Autowired
    PermissionRepo permissionRepo;
    @Autowired
    private Core core;
    @Autowired
    private Session session;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        try {
            String requestURI = request.getRequestURI();
            if (!requestURI.startsWith("/console") && !requestURI.equals("/favicon.ico")) {

                //if (request.getHeader("mode") != null && !request.getHeader("mode").equals("test")) {
                Permission.PermissionType requiredPermission = GC.PERMISSION_TABLE.get(requestURI);
                if (requiredPermission == null) {
                    throw new ProcessException(GC.Errors.UNKNOWN_SERVICE);
                }
                if (requiredPermission == Permission.PermissionType.NONE) {

                } else {
                    // if there is some permission needed
                    // check if sheet and user are existed
                    core.assertSheetExistence();
                    core.assertUserExistence();
                    Permission userPermission = permissionRepo.findByUserAndSheet(session.getUser(), session.getSheet());
                    if (userPermission == null || requiredPermission.getValue() > userPermission.getType().getValue()) {
                        throw new ProcessException(GC.Errors.ACCESS_DENIED);
                    }
                }
            }
            chain.doFilter(req, res);
        } catch (ProcessException ex) {
            response.setStatus(HttpStatus.OK.value());
            FinalResponse resp = new FinalResponse();
            resp.code = ex.getError().getValue();
            resp.message = ex.getError().name();
            response.getWriter().write(GV.OBJECT_MAPPER.writeValueAsString(resp));

        } catch (Exception ex) {
            response.setStatus(HttpStatus.OK.value());
            FinalResponse resp = new FinalResponse();
            resp.code = GC.Errors.UNKNOWN_ERROR.getValue();
            resp.message = GC.Errors.UNKNOWN_ERROR.name();
            response.getWriter().write(GV.OBJECT_MAPPER.writeValueAsString(resp));
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
