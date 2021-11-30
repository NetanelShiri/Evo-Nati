package ett.servlets;

import UI.UIAdapter;
import ett.utils.ServletUtils;
import com.google.gson.Gson;
import engine.ett.FileManager;
import engine.ett.SingleFileEntry;
import javafx.scene.text.TextFlow;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;


public class PresentDataServlet  extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            Gson gson = new Gson();
            Enumeration<String> keys = request.getParameterNames();
            String fileName = keys.nextElement();
            fileName = trimFile(fileName);


            FileManager fileManager = ServletUtils.getFileManager(getServletContext());
            UIAdapter adapter = fileManager.getFileAdapter(fileName);
            fileManager.setChosenAdapter(fileName);

            String data = adapter.printage();
            String json = gson.toJson(data);

            out.println(json);

            out.flush();
        }
    }

    public synchronized String trimFile(String fileName){
        int index = fileName.lastIndexOf('(');
        return fileName.substring(0,index);

    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
