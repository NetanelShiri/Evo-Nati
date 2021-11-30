package ett.servlets;

import UI.UIAdapter;
import com.google.gson.Gson;
import engine.ett.FileManager;
import ett.constants.Constants;
import ett.utils.ServletUtils;
import ett.utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DataServlet", urlPatterns = { "/pages/algoroom/data" })
public class DataServlet extends HttpServlet{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        response.setContentType("application/json");
        FileManager fileManager = ServletUtils.getFileManager(getServletContext());
        UIAdapter adapter = fileManager.getChosenAdapter();

        Set<Map.Entry<String, String[]>> mapset = request.getParameterMap().entrySet();
        List<Map.Entry<String, String[]>> maplist = new ArrayList<>(mapset);

        sendToEngine(maplist,adapter);


        try (PrintWriter out = response.getWriter()) {

            Gson gson = new Gson();

            String data = adapter.printage();
            String json = gson.toJson(data);

            out.println(json);

            out.flush();
        }
    }

    public synchronized void sendToEngine(List<Map.Entry<String, String[]>> data,UIAdapter adapter){
        String name;
        String value;

        for(int i = 0; i < data.size();i++) {
            Map.Entry<String, String[]> entry = data.get(i);

            name = entry.getKey();
            value = entry.getValue()[0];
            if(name.equals("Run")){
                Runnable r = new Runnable() {
                    public void run() {
                        adapter.runAlgorithm(100,80,30);
                    }
                };

                new Thread(r).start();
            }

            if(name.equals("Tournament") || name.equals("Roulette Wheel") || name.equals("Trunction")){
                adapter.manipulateSelection(name,0.5);
            }else if(name.equals("elitism")) {
                adapter.manipulateElitism(Integer.parseInt(value));
            }
            else if(name.equals("population")){
                adapter.manipulatePopulation(Integer.parseInt(value));
            }
            else if(name.equals("Cover")){
                System.out.println("got here");
                int cuttingpts = data.get(i+1).getValue()[0] != null ? Integer.parseInt(data.get(i+1).getValue()[0]) : 0;
                adapter.manipulateCrossover(value,"Day",cuttingpts);
                i+=1;
            }
            else if(name.equals("Flipping") || name.equals("Sizer")){
                String mutName = name;
                System.out.println("got here - name is " + name);
                String component = data.get(i+1).getValue()[0] != null ? data.get(i+1).getValue()[0] : "Days";
                System.out.println("got here -  " + component);
                int tupples = data.get(i+2).getValue()[0] != null ? Integer.parseInt(data.get(i+2).getValue()[0]) : 0;
                System.out.println("got here -  " + tupples);
                double probability =  data.get(i+3).getValue()[0] != null ? Double.parseDouble(data.get(i+3).getValue()[0]) : 0;;
                System.out.println("got here -  " + probability);
                adapter.manipulateMutation(mutName,component, tupples,probability);
                i+=3;
            }
            System.out.println(name + " -~~- " + value);
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
