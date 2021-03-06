package ett.servlets;

import ett.utils.ServletUtils;
import com.google.gson.Gson;
import engine.ett.FileManager;
import engine.ett.SingleFileEntry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class FileListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            Gson gson = new Gson();
            FileManager fileManager = ServletUtils.getFileManager(getServletContext());

            List<SingleFileEntry> filelist = fileManager.getChatEntries(0);
            List<String> tempList = new ArrayList<>();
            for(SingleFileEntry entry: filelist){
                tempList.add(entry.getFileString()+"("+entry.getUsername()+")");
            }
            String json = gson.toJson(tempList);
            out.println(json);
            out.flush();
        }
    }

    private static class FileAndVersion {

        final private List<SingleFileEntry> entries;
        final private int version;

        public FileAndVersion(List<SingleFileEntry> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
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
