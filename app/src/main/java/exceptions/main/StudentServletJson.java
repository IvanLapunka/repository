package exceptions.main;

import exceptions.pojo.Student;
import exceptions.service.StudentService;
import exceptions.service.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@WebServlet("/student")
public class StudentServletJson extends JsonController {
    private static String ID = "id";
    StudentService studentService = StudentServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String parameter = req.getParameter(ID);
        if (Objects.isNull(parameter)) {
            getAllStudents(resp);
        } else {
            getOneStudent(resp, parameter);
        }
    }

    private void getAllStudents(HttpServletResponse resp) throws IOException {
        toJson(studentService.getAllStudents(), resp);
    }

    private void getOneStudent(HttpServletResponse resp, String parameter) throws IOException {
        toJson(studentService.getStudent(Integer.parseInt(parameter)), resp);
    }
}
