package njnu.opengms.container.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName ServletDefault
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@WebServlet ("/ServletDefault")
public class ServletDefault extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write(1);
    }
}
