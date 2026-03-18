package gui;

import dao.*;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HotelReservationSystem extends JFrame {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    // DAOs
    private final HotelDAO hotelDAO            = new HotelDAO();
    private final RoomDAO  roomDAO             = new RoomDAO();
    private final GuestDAO guestDAO            = new GuestDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    // ─── Constructor ──────────────────────────────────────────────────────────
    public HotelReservationSystem() {
        setTitle("Hotel Reservation System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildMainMenu();
        setVisible(true);
    }

    // ─── Main Menu ────────────────────────────────────────────────────────────
    private void buildMainMenu() {
        getContentPane().removeAll();
        setLayout(new GridLayout(5, 1, 10, 10));

        String[] labels = {"Add Hotel", "Add Room", "Add Guest", "Add Reservation", "Retrieve Data"};
        Runnable[] actions = {
            this::showAddHotelDialog,
            this::showAddRoomDialog,
            this::showAddGuestDialog,
            this::showAddReservationDialog,
            this::showRetrieveDataDialog
        };

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            final int idx = i;
            btn.addActionListener(e -> actions[idx].run());
            add(btn);
        }
        revalidate();
        repaint();
    }

    // ─── Add Hotel Dialog ─────────────────────────────────────────────────────
    private void showAddHotelDialog() {
        JTextField nameField      = new JTextField();
        JTextField locationField  = new JTextField();
        JTextField amenitiesField = new JTextField();

        Object[] fields = {
            "Hotel Name:",     nameField,
            "Location:",       locationField,
            "Amenities:",      amenitiesField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Hotel",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Hotel hotel = new Hotel(
                nameField.getText().trim(),
                locationField.getText().trim(),
                amenitiesField.getText().trim()
            );
            boolean ok = hotelDAO.addHotel(hotel);
            showMsg(ok ? "Hotel added successfully!" : "Failed to add hotel.");
        }
    }

    // ─── Add Room Dialog ──────────────────────────────────────────────────────
    private void showAddRoomDialog() {
        JTextField hotelIdField  = new JTextField();
        JTextField roomNumField  = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Single", "Double", "Suite"});
        JTextField priceField    = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Available", "Occupied", "Under Maintenance"});

        Object[] fields = {
            "Hotel ID:",    hotelIdField,
            "Room Number:", roomNumField,
            "Type:",        typeBox,
            "Price/Night:", priceField,
            "Status:",      statusBox
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Room",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Room room = new Room(
                    Integer.parseInt(hotelIdField.getText().trim()),
                    roomNumField.getText().trim(),
                    (String) typeBox.getSelectedItem(),
                    Double.parseDouble(priceField.getText().trim()),
                    (String) statusBox.getSelectedItem()
                );
                boolean ok = roomDAO.addRoom(room);
                showMsg(ok ? "Room added successfully!" : "Failed to add room.");
            } catch (NumberFormatException e) {
                showMsg("Invalid Hotel ID or Price. Please enter numbers.");
            }
        }
    }

    // ─── Add Guest Dialog ─────────────────────────────────────────────────────
    private void showAddGuestDialog() {
        JTextField nameField  = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] fields = {
            "Name:",  nameField,
            "Email:", emailField,
            "Phone:", phoneField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Guest",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Guest guest = new Guest(
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim()
            );
            boolean ok = guestDAO.addGuest(guest);
            showMsg(ok ? "Guest added successfully!" : "Failed to add guest.");
        }
    }

    // ─── Add Reservation Dialog ───────────────────────────────────────────────
    private void showAddReservationDialog() {
        JTextField guestIdField   = new JTextField();
        JTextField roomIdField    = new JTextField();
        JTextField checkInField   = new JTextField("yyyy-MM-dd");
        JTextField checkOutField  = new JTextField("yyyy-MM-dd");

        Object[] fields = {
            "Guest ID:",        guestIdField,
            "Room ID:",         roomIdField,
            "Check-In Date:",   checkInField,
            "Check-Out Date:",  checkOutField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add Reservation",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int guestId  = Integer.parseInt(guestIdField.getText().trim());
                int roomId   = Integer.parseInt(roomIdField.getText().trim());
                Date checkIn  = SDF.parse(checkInField.getText().trim());
                Date checkOut = SDF.parse(checkOutField.getText().trim());

                if (!checkOut.after(checkIn)) {
                    showMsg("Check-out date must be after check-in date.");
                    return;
                }

                // Calculate cost
                Room room = roomDAO.getAllRooms().stream()
                        .filter(r -> r.getRoomId() == roomId).findFirst().orElse(null);
                if (room == null) { showMsg("Room not found."); return; }

                long days = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
                double totalCost = days * room.getPrice();

                Reservation res = new Reservation(guestId, roomId, checkIn, checkOut, totalCost);
                boolean ok = reservationDAO.addReservation(res);
                if (ok) {
                    roomDAO.updateRoomStatus(roomId, "Occupied");
                    showMsg("Reservation added! Total cost: ₹" + totalCost);
                } else {
                    showMsg("Failed to add reservation.");
                }
            } catch (NumberFormatException e) {
                showMsg("Invalid ID. Please enter numbers for Guest ID and Room ID.");
            } catch (ParseException e) {
                showMsg("Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    // ─── Retrieve Data Dialog ─────────────────────────────────────────────────
    private void showRetrieveDataDialog() {
        String[] options = {"Hotels", "Rooms", "Guests", "Reservations"};
        int choice = JOptionPane.showOptionDialog(this, "Select data to view:", "Retrieve Data",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0: showTable("Hotels",       buildHotelTable());       break;
            case 1: showTable("Rooms",        buildRoomTable());        break;
            case 2: showTable("Guests",       buildGuestTable());       break;
            case 3: showTable("Reservations", buildReservationTable()); break;
        }
    }

    // ─── Table Builders ───────────────────────────────────────────────────────
    private DefaultTableModel buildHotelTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Name", "Location", "Amenities"}, 0);
        for (Hotel h : hotelDAO.getAllHotels())
            model.addRow(new Object[]{h.getHotelId(), h.getName(), h.getLocation(), h.getAmenities()});
        return model;
    }

    private DefaultTableModel buildRoomTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Hotel ID", "Room No", "Type", "Price", "Status"}, 0);
        for (Room r : roomDAO.getAllRooms())
            model.addRow(new Object[]{r.getRoomId(), r.getHotelId(), r.getRoomNumber(),
                    r.getType(), r.getPrice(), r.getStatus()});
        return model;
    }

    private DefaultTableModel buildGuestTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Name", "Email", "Phone"}, 0);
        for (Guest g : guestDAO.getAllGuests())
            model.addRow(new Object[]{g.getGuestId(), g.getName(), g.getEmail(), g.getPhone()});
        return model;
    }

    private DefaultTableModel buildReservationTable() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Guest ID", "Room ID", "Check-In", "Check-Out", "Total Cost"}, 0);
        for (Reservation res : reservationDAO.getAllReservations())
            model.addRow(new Object[]{res.getReservationId(), res.getGuestId(), res.getRoomId(),
                    SDF.format(res.getCheckInDate()), SDF.format(res.getCheckOutDate()), res.getTotalCost()});
        return model;
    }

    // ─── Generic Table Viewer ─────────────────────────────────────────────────
    private void showTable(String title, DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 300));

        JOptionPane.showMessageDialog(this, scrollPane, title + " Data",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── Utility ──────────────────────────────────────────────────────────────
    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // ─── Main ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelReservationSystem::new);
    }
}
