package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.Util.UserServiceUtil;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Users;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private Users user;
    private List<Users> userlist;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserBookingService(Users user1) throws IOException {
        this.user = user1;
        File file = new File("app/src/main/java/ticket/booking/localdb/users.json");
        userlist = objectMapper.readValue(file, new TypeReference<List<Users>>() {
        });
    }

    public UserBookingService() throws IOException{
        File file = new File("app/src/main/java/ticket/booking/localdb/users.json");
        userlist = objectMapper.readValue(file, new TypeReference<List<Users>>() {
        });
    }

    public boolean loginUser(){
        Optional<Users> foundUser = userlist.stream().filter(
                user1 -> {
                    return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
                }).findFirst();

        return foundUser.isPresent();
    }

    public boolean signup(Users user){
        try{
            userlist.add(user);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File("app/src/main/java/ticket/booking/localdb/users.json");
        objectMapper.writeValue(usersFile, userlist);
    }

    public void fetchBookings(){
        user.printTickets();
    }

    public void cancelBooking(String ticketId) throws IOException{
        Optional<Ticket> foundTicket = user.getTicketsBooked().stream().filter(
                ticket -> {
                    if(ticket.getTicketId().equals(ticketId)){
                        user.getTicketsBooked().remove(ticket);
                        return true;
                    }
                    return false;
                }).findFirst();
        saveUserListToFile();
    }

}
