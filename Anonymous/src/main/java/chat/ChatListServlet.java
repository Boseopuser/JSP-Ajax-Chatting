package chat;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * Servlet implementation class ChatListServlet
 */
 
@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 어떠한 list인지 그 Type을 나타내줌
        String listType = request.getParameter("listType");
        // 만약 없다면 공백 출력
        if (listType == null || listType.equals(""))
            response.getWriter().write("");
         
        else if (listType == "close" || listType.equals("close")) {
            ChatDAO chatDAO = new ChatDAO();
            response.getWriter().write(chatDAO.eraseDB());
        } 
         
        else {
            try {
                Integer.parseInt(listType);
                response.getWriter().write(getID(listType));
            } catch (Exception e) {
                response.getWriter().write("");
            }
        }
    }
 
    public String getID(String chatID) {
 
        StringBuffer result = new StringBuffer("");
        result.append("{\"result\":[");
        ChatDAO chatDAO = new ChatDAO();
        // SimpleDateFormat (현재 날짜를 나타내는 format)
        ArrayList<Chat> chatList = chatDAO.getChatListByRecent(chatID);
 
        // 결과가 있을 때 즉 오늘에 시작한 메시지를 모두 담아와서 보내줌
        for (int i = 0; i < chatList.size(); i++) {
            result.append("[{\"value\":\"" + chatList.get(i).getChatName() + "\"},");
            result.append("{\"value\":\"" + chatList.get(i).getChatContent() + "\"},");
            result.append("{\"value\":\"" + chatList.get(i).getChatTime() + "\"}]");
            // i 가 마지막원소가 아니라면 뒤에 더 있다는 의미
            if (i != chatList.size() - 1)
                result.append(",");
        }
        result.append("],\"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
        return result.toString();
    }
}