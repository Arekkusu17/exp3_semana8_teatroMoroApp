/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.teatromoros8;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Alex Fernández
 */
public class TeatroMoroS8 {
    static Scanner scanner = new Scanner(System.in);

    // Constantes
    static final int MAX_SALES = 100;
    static final int ROWS = 5;
    static final int COLUMNS = 10;
    static final double BASE_PRICE = 10000.0;

    // Listas
    static List<String> discounts = new ArrayList<>();
    static List<Reservation> reservations = new ArrayList<>();

    // Arreglos
    static Customer[] customers = new Customer[MAX_SALES]; // Arreglo de clientes
    static int[] saleIds = new int[MAX_SALES]; // Arreglo de IDs de ventas
    static boolean[][] seatMap = new boolean[ROWS][COLUMNS]; // Arreglo para gestionar los asientos

    // Variables auxiliares
    static int saleIndex = 0;

    // Clase para representar una reserva
    static class Reservation {
        int reservationId;
        int customerId; // ID del cliente relacionado
        String customerName; // Nombre del cliente
        String discountType; // Tipo de descuento aplicado
        int row;
        int column;
        double finalPrice;

        public Reservation(int reservationId, int customerId, String customerName, String discountType, int row, int column, double finalPrice) {
            this.reservationId = reservationId;
            this.customerId = customerId;
            this.customerName = customerName;
            this.discountType = discountType;
            this.row = row;
            this.column = column;
            this.finalPrice = finalPrice;
        }

        @Override
        public String toString() {
            return "Reserva #" + reservationId + ": Cliente " + customerName + " (ID: " + customerId + "), Descuento: " + discountType + ", Asiento [" + row + "," + column + "], Precio final: $" + finalPrice;
        }
    }

    // Clase para representar un cliente
    static class Customer {
        int id;
        String name;
        String discountType;

        public Customer(int id, String name, String discountType) {
            this.id = id;
            this.name = name;
            this.discountType = discountType;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public String getDiscountType() {
            return discountType;
        }
    }

    public static void main(String[] args) {
        initializeDiscounts(); // Inicializamos la lista de descuentos
        showMenu();
    }

    static void initializeDiscounts() {
        discounts.add("estudiante:0.10");
        discounts.add("tercera edad:0.15");
        discounts.add("ninguno:0.0");
    }

    static void showMenu() {
        int option;
        do {
            System.out.println("\n--- Teatro Moro - Sistema Optimizado ---");
            System.out.println("1. Vender entrada");
            System.out.println("2. Mostrar reservas");
            System.out.println("3. Eliminar reserva");
            System.out.println("4. Actualizar asiento de reserva");
            System.out.println("5. Salir");
            option = getValidOption(1, 5);
            switch (option) {
                case 1:
                    sellTicket();
                    break;
                case 2:
                    showReservations();
                    break;
                case 3:
                    deleteReservation();
                    break;
                case 4:
                    updateReservation();
                    break;
                case 5:
                    displaySeatMap();
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }

    static void sellTicket() {
        if (saleIndex >= MAX_SALES) {
            System.out.println("Se alcanzó el límite de ventas.");
            return;
        }

        System.out.print("Ingrese el nombre del cliente: ");
        String name = scanner.nextLine();

        System.out.print("¿Es estudiante/tercera edad/ninguno? (Ingresar solo una categoría): ");
        String discountType = getValidDiscountType();

        int row = getValidRow();

        int column = getValidSeat();

        if (seatMap[row][column]) {
            System.out.println("El asiento ya está ocupado.");
            return;
        }

        double discount = calculateDiscount(discountType);
        double finalPrice = BASE_PRICE - (BASE_PRICE * discount);

        // Crear nuevo cliente
        Customer customer = new Customer(saleIndex + 1, name, discountType);
        customers[saleIndex] = customer;

        // Crear nueva reserva
        Reservation reservation = new Reservation(saleIndex + 1, customer.getId(), customer.getName(), discountType, row, column, finalPrice);
        reservations.add(reservation);

        // Marcar el asiento como ocupado
        seatMap[row][column] = true;

        // Guardar ID de la venta
        saleIds[saleIndex] = customer.getId();

        System.out.println("¡Entrada vendida con éxito!");
        System.out.println(reservation);

        saleIndex++;
    }

    static double calculateDiscount(String discountType) {
        // Buscar el tipo de descuento en la lista
        for (String discount : discounts) {
            String[] parts = discount.split(":");
            if (parts[0].equals(discountType)) {
                return Double.parseDouble(parts[1]);
            }
        }
        return 0.0; // Si no encuentra, no hay descuento
    }

    static void showReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No hay reservas registradas.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    static void deleteReservation() {
        System.out.print("Ingrese el ID de la reserva a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Reservation r = findReservationById(id);
        if (r != null) {
            // Liberar el asiento
            seatMap[r.row][r.column] = false;
            // Eliminar el cliente de la lista de clientes
            customers[r.customerId - 1] = null;  // Eliminar el cliente del arreglo (el cliente tiene un ID basado en el índice de venta)
            
            // Eliminar el ID de venta del arreglo saleIds
            saleIds[r.customerId - 1] = 0;  // Eliminar el ID de venta

            // Eliminar la reserva de la lista de reservas
            reservations.remove(r);
            System.out.println("Reserva eliminada correctamente.");
        } else {
            System.out.println("No se encontró una reserva con ese ID.");
        }
    }

    static void updateReservation() {
        System.out.print("Ingrese el ID de la reserva a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Reservation r = findReservationById(id);
        if (r != null) {
            // Liberar el asiento anterior
            seatMap[r.row][r.column] = false;

            int newRow = getValidRow();
            int newColumn = getValidSeat();

            if (seatMap[newRow][newColumn]) {
                System.out.println("El nuevo asiento ya está ocupado.");
                return;
            }

            // Asignar el nuevo asiento
            r.row = newRow;
            r.column = newColumn;
            seatMap[newRow][newColumn] = true;

            System.out.println("Reserva actualizada correctamente.");
        } else {
            System.out.println("No se encontró una reserva con ese ID.");
        }
    }

    // UTILIDADES
    static void displaySeatMap() {
        System.out.println("\nMapa de asientos (X = ocupado, O = libre):");
        for (int i = 0; i < ROWS; i++) {
            System.out.print("Fila " + i + ": ");
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(seatMap[i][j] ? "X " : "O ");
            }
            System.out.println();
        }
    }

    static Reservation findReservationById(int id) {
        for (Reservation r : reservations) {
            if (r.reservationId == id) {
                return r;
            }
        }
        return null;
    }

    static int getValidRow() {
        int row = -1;
        boolean validRow = false;

        while(!validRow) {
            System.out.print("Seleccione fila (0-4): ");
            if (scanner.hasNextInt()) {
                row = scanner.nextInt();
                scanner.nextLine();
                if (row >= 0 && row < 5) {
                    validRow = true;
                } else {
                    System.out.println("Fila fuera de rango.");
                }
            } else {
                System.out.println("Entrada inválida. Ingrese un número.");
                scanner.nextLine(); 
            }
        }

        return row;
    }

    static int getValidSeat() {
        int seat = -1;
        boolean validSeat = false;

        while (!validSeat) {
            System.out.print("Seleccione asiento (0-9): ");
            if (scanner.hasNextInt()) {
                seat = scanner.nextInt();
                scanner.nextLine();

                if (seat >= 0 && seat < 10) {
                    validSeat = true;
                } else {
                    System.out.println("Asiento fuera de rango.");
                }
            } else {
                System.out.println("Entrada inválida. Ingrese un número: ");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
        return seat;
    }

    static String getValidDiscountType() {
        String discountType = scanner.nextLine().toLowerCase();
        while (!discountType.equals("estudiante") && !discountType.equals("tercera edad") && !discountType.equals("ninguno")) {
            System.out.print("Tipo de descuento inválido. Ingrese 'estudiante', 'tercera edad' o 'ninguno': ");
            discountType = scanner.nextLine().toLowerCase();
        }
        return discountType;
    }

    static int getValidOption(int min, int max) {
        int option = -1;
        boolean validOption = false;

        while (!validOption) {
            System.out.print("Seleccione una opción (" + min + "-" + max + "): ");
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine(); 
                if (option >= min && option <= max) {
                    validOption = true;
                } else {
                    System.out.println("Opción fuera de rango.");
                }
            } else {
                System.out.println("Entrada inválida. Ingrese un número.");
                scanner.nextLine(); 
            }
        }

        return option;
    }
}
