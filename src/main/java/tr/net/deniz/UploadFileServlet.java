/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.net.deniz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jboss.logging.Logger;

/**
 *
 * @author yukseldeniz
 */
@WebServlet(name = "UploadFileServlet", urlPatterns = {"/UploadFileServlet"})
@MultipartConfig
public class UploadFileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Resource
    UserTransaction transaction;
    @PersistenceContext(unitName = "com.mycompany_suggestion_system_v2_war_1.0-SNAPSHOTPU")
    EntityManager em;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher rdObj = null;

        String token = request.getParameter("token");        

        if (SuggestionService.sessionMap.containsKey(token)) {
            String suggId = request.getParameter("suggId");
            Long suggIdLong = Long.parseLong(suggId);

            final Part filePart = request.getPart("fileUpload");
            String fileName = getFileName(filePart);

            if (!fileName.equals("")) {
                try {
                    ByteArrayOutputStream byteOut = null;
                    InputStream fileContent = null;

                    // Read the file...
                    byteOut = new ByteArrayOutputStream();
                    fileContent = filePart.getInputStream();

                    int read = 0;
                    final byte[] bytes = new byte[16 * 1024];

                    while ((read = fileContent.read(bytes)) != -1) {
                        byteOut.write(bytes, 0, read);
                    }
                    Logger.getLogger(UploadFileServlet.class.getName()).log(Logger.Level.INFO, new String(byteOut.toByteArray()));
                    byte[] fileInBytes = byteOut.toByteArray(); //this is the whole file in a byte array.

                    transaction.begin();

                    FileInstance fileObj = new FileInstance(fileName, fileInBytes);
                    em.persist(fileObj);

                    Suggestion suggestion = em.find(Suggestion.class, suggIdLong);
                    Long persistedFileId = fileObj.getFileId();

                    suggestion.setHasFile(true);
                    suggestion.setFileIdRef(persistedFileId);
                    em.persist(suggestion);

                    transaction.commit();
                    
                    response.getWriter().write("Successfully uploaded suggestion with file.");
                    //response.sendRedirect(token);
                    //response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                    // redirect?

                } catch (RollbackException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HeuristicMixedException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HeuristicRollbackException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalStateException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SystemException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotSupportedException ex) {
                    java.util.logging.Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                response.getWriter().write("Successfully uploaded suggestion without file.");
            }

        }

        /*
        suggestion.setHasFile(true);
        suggestion.setFileInstance(fileObj);

        transaction.begin();
        em.persist(suggestion);
        em.persist(fileObj);
        transaction.commit();

        //Redirect.
        request.getSession().setAttribute("suggestionEntity", suggestion);
        request.setAttribute("resultMsg", "Suggestion added successfully.");
        rdObj = request.getRequestDispatcher("AddSuggestionResult.jsp");
        rdObj.forward(request, response);
         */
    }

    private String getFileName(Part filePart) {
        String header = filePart.getHeader("content-disposition");
        String name = header.substring(header.indexOf("filename=\"") + 10);
        return name.substring(0, name.indexOf("\""));
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
