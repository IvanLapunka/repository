package exceptions.main;

import by.pojo.Teacher;
import exceptions.service.TeacherService;
import exceptions.service.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/teacher")
public class TeacherServletJson extends JsonController {
    private static final String ID = "id";
    private TeacherService teacherService = TeacherServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teacherId = req.getParameter(ID);
        if (teacherId != null) {
            toJson(teacherService.getTeacher(Integer.parseInt(teacherId)), resp);
        } else {
            toJson(teacherService.getAllTeachers(), resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Teacher teacher = toObject(Teacher.class, req);
        toJson(teacherService.saveTeacher(teacher), resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teacherId = req.getParameter(ID);
        if (teacherId != null) {
            toJson(teacherService.deleteTeacher(Integer.parseInt(teacherId)), resp);
        }
    }
}
