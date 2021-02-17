package exceptions.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class JsonController extends HttpServlet {
    protected void toJson(Object obj, HttpServletResponse resp) throws IOException {
        obj = (obj instanceof Optional) ? ((Optional<?>) obj).orElse(null) : obj;
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(obj);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println(result);
        writer.flush();
    }

    protected <T> T toObject(Class<T> clazz, HttpServletRequest req) throws IOException {
        String json = req.getReader().lines().collect(Collectors.joining());
        ObjectMapper objectMapper = new ObjectMapper();
        final T t = objectMapper.readValue(json, clazz);
        return t;
    }
}
