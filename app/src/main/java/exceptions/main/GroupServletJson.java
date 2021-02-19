package exceptions.main;

import exceptions.pojo.Group;
import exceptions.service.GroupService;
import exceptions.service.GroupServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/group")
public class GroupServletJson extends JsonController {
    private static final String ID = "id";
    private GroupService groupService = GroupServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupId = req.getParameter(ID);
        if (groupId != null) {
            toJson(groupService.getGroup(Integer.parseInt(groupId)), resp);
        } else {
            toJson(groupService.getAllGroups(), resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Group group = toObject(Group.class, req);
        toJson(groupService.saveGroup(group), resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String groupId = req.getParameter(ID);
        if (groupId != null) {
            toJson(groupService.deleteGroup(Integer.parseInt(groupId)), resp);
        }
    }
}
