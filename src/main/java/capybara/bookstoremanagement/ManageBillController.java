// Source code is decompiled from a .class file using FernFlower decompiler.
package capybara.bookstoremanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class DatabaseUtil {
   private static final String URL = "jdbc:sqlite:books.db";

   public DatabaseUtil() {
   }

   public static Connection connect() throws SQLException {
      return DriverManager.getConnection("jdbc:sqlite:books.db");
   }

   public static void createTables() {
      String createBooksTable = "CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY AUTOINCREMENT, bookId TEXT NOT NULL UNIQUE, title TEXT NOT NULL, author TEXT NOT NULL, price REAL NOT NULL);";
      String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL, role TEXT NOT NULL CHECK(role IN ('admin', 'manager', 'employee')));";
      String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, phone TEXT NOT NULL);";
      String createEmployeesTable = "CREATE TABLE IF NOT EXISTS employees (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, position TEXT NOT NULL, salary REAL NOT NULL);";
      String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY AUTOINCREMENT, customer TEXT NOT NULL, bookId TEXT NOT NULL, quantity INTEGER NOT NULL, totalPrice REAL NOT NULL, FOREIGN KEY(bookId) REFERENCES books(bookId));";
      String createToysTable = "CREATE TABLE IF NOT EXISTS toys (id TEXT PRIMARY KEY, name TEXT NOT NULL, origin TEXT NOT NULL, age_limit TEXT NOT NULL, price REAL NOT NULL);";

      try {
         Throwable var6 = null;
         Object var7 = null;

         try {
            Connection conn = connect();

            try {
               Statement stmt = conn.createStatement();

               try {
                  stmt.execute(createBooksTable);
                  stmt.execute(createAccountsTable);
                  stmt.execute(createCustomersTable);
                  stmt.execute(createEmployeesTable);
                  stmt.execute(createOrdersTable);
                  stmt.execute(createToysTable);
                  System.out.println("Tables 'books', 'accounts', 'customers', 'employees', 'toys' and 'orders' created or already exist.");
               } finally {
                  if (stmt != null) {
                     stmt.close();
                  }

               }
            } catch (Throwable var23) {
               if (var6 == null) {
                  var6 = var23;
               } else if (var6 != var23) {
                  var6.addSuppressed(var23);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var6;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (Throwable var24) {
            if (var6 == null) {
               var6 = var24;
            } else if (var6 != var24) {
               var6.addSuppressed(var24);
            }

            throw var6;
         }
      } catch (SQLException var25) {
         var25.printStackTrace();
      }

   }

   public static boolean validateUser(String username, String password) {
      String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";

      try {
         Throwable var3 = null;
         Object var4 = null;

         try {
            Connection conn = connect();

            boolean var10000;
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  pstmt.setString(1, username);
                  pstmt.setString(2, password);
                  ResultSet rs = pstmt.executeQuery();
                  var10000 = rs.next();
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var21) {
               if (var3 == null) {
                  var3 = var21;
               } else if (var3 != var21) {
                  var3.addSuppressed(var21);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var3;
            }

            if (conn != null) {
               conn.close();
            }

            return var10000;
         } catch (Throwable var22) {
            if (var3 == null) {
               var3 = var22;
            } else if (var3 != var22) {
               var3.addSuppressed(var22);
            }

            throw var3;
         }
      } catch (SQLException var23) {
         System.out.println(var23.getMessage());
         return false;
      }
   }

   public static boolean addUser(String username, String password) {
      String sql = "INSERT INTO accounts(username, password) VALUES(?, ?)";

      try {
         Throwable var3 = null;
         Object var4 = null;

         try {
            Connection conn = connect();

            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  pstmt.setString(1, username);
                  pstmt.setString(2, password);
                  pstmt.executeUpdate();
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var20) {
               if (var3 == null) {
                  var3 = var20;
               } else if (var3 != var20) {
                  var3.addSuppressed(var20);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var3;
            }

            if (conn != null) {
               conn.close();
            }

            return true;
         } catch (Throwable var21) {
            if (var3 == null) {
               var3 = var21;
            } else if (var3 != var21) {
               var3.addSuppressed(var21);
            }

            throw var3;
         }
      } catch (SQLException var22) {
         System.out.println(var22.getMessage());
         return false;
      }
   }

   public static void createBook(String bookId, String title, String author, double price) throws SQLException {
      String sql = "INSERT INTO books(bookId, title, author, price) VALUES(?, ?, ?, ?)";
      Throwable var6 = null;
      Object var7 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, bookId);
               pstmt.setString(2, title);
               pstmt.setString(3, author);
               pstmt.setDouble(4, price);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var20) {
            if (var6 == null) {
               var6 = var20;
            } else if (var6 != var20) {
               var6.addSuppressed(var20);
            }

            if (conn != null) {
               conn.close();
            }

            throw var6;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var21) {
         if (var6 == null) {
            var6 = var21;
         } else if (var6 != var21) {
            var6.addSuppressed(var21);
         }

         throw var6;
      }
   }

   public static void updateBook(int id, String title, String author, double price) throws SQLException {
      String sql = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";
      Throwable var6 = null;
      Object var7 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, title);
               pstmt.setString(2, author);
               pstmt.setDouble(3, price);
               pstmt.setInt(4, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var20) {
            if (var6 == null) {
               var6 = var20;
            } else if (var6 != var20) {
               var6.addSuppressed(var20);
            }

            if (conn != null) {
               conn.close();
            }

            throw var6;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var21) {
         if (var6 == null) {
            var6 = var21;
         } else if (var6 != var21) {
            var6.addSuppressed(var21);
         }

         throw var6;
      }
   }

   public static void deleteBook(int id) throws SQLException {
      String sql = "DELETE FROM books WHERE id = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setInt(1, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var16) {
            if (var2 == null) {
               var2 = var16;
            } else if (var2 != var16) {
               var2.addSuppressed(var16);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var17) {
         if (var2 == null) {
            var2 = var17;
         } else if (var2 != var17) {
            var2.addSuppressed(var17);
         }

         throw var2;
      }
   }

   public static ResultSet getAllBooks() throws SQLException {
      String sql = "SELECT * FROM books";
      Connection conn = connect();
      return conn.createStatement().executeQuery(sql);
   }

   public static boolean isBookIdExists(String bookId) throws SQLException {
      String sql = "SELECT COUNT(*) FROM books WHERE bookId = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         boolean var10000;
         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, bookId);
               ResultSet rs = pstmt.executeQuery();
               rs.next();
               var10000 = rs.getInt(1) > 0;
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var17) {
            if (var2 == null) {
               var2 = var17;
            } else if (var2 != var17) {
               var2.addSuppressed(var17);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var18) {
         if (var2 == null) {
            var2 = var18;
         } else if (var2 != var18) {
            var2.addSuppressed(var18);
         }

         throw var2;
      }
   }

   public static String generateUniqueBookId() throws SQLException {
      Random random = new Random();

      String bookId;
      do {
         bookId = String.format("978-0-%05d-%03d-%d", random.nextInt(100000), random.nextInt(1000), random.nextInt(10));
      } while(isBookIdExists(bookId));

      return bookId;
   }

   public static void createCustomer(String name, String email, String phone) throws SQLException {
      String sql = "INSERT INTO customers(name, email, phone) VALUES(?, ?, ?)";
      Throwable var4 = null;
      Object var5 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, name);
               pstmt.setString(2, email);
               pstmt.setString(3, phone);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var18) {
            if (var4 == null) {
               var4 = var18;
            } else if (var4 != var18) {
               var4.addSuppressed(var18);
            }

            if (conn != null) {
               conn.close();
            }

            throw var4;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var19) {
         if (var4 == null) {
            var4 = var19;
         } else if (var4 != var19) {
            var4.addSuppressed(var19);
         }

         throw var4;
      }
   }

   public static void updateCustomer(int id, String name, String email, String phone) throws SQLException {
      String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
      Throwable var5 = null;
      Object var6 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, name);
               pstmt.setString(2, email);
               pstmt.setString(3, phone);
               pstmt.setInt(4, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var19) {
            if (var5 == null) {
               var5 = var19;
            } else if (var5 != var19) {
               var5.addSuppressed(var19);
            }

            if (conn != null) {
               conn.close();
            }

            throw var5;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var20) {
         if (var5 == null) {
            var5 = var20;
         } else if (var5 != var20) {
            var5.addSuppressed(var20);
         }

         throw var5;
      }
   }

   public static void deleteCustomer(int id) throws SQLException {
      String sql = "DELETE FROM customers WHERE id = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setInt(1, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var16) {
            if (var2 == null) {
               var2 = var16;
            } else if (var2 != var16) {
               var2.addSuppressed(var16);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var17) {
         if (var2 == null) {
            var2 = var17;
         } else if (var2 != var17) {
            var2.addSuppressed(var17);
         }

         throw var2;
      }
   }

   public static ResultSet getAllCustomers() throws SQLException {
      String sql = "SELECT * FROM customers";
      Connection conn = connect();
      return conn.createStatement().executeQuery(sql);
   }

   public static void createEmployee(String name, String position, double salary) throws SQLException {
      String sql = "INSERT INTO employees(name, position, salary) VALUES(?, ?, ?)";
      Throwable var5 = null;
      Object var6 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, name);
               pstmt.setString(2, position);
               pstmt.setDouble(3, salary);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var19) {
            if (var5 == null) {
               var5 = var19;
            } else if (var5 != var19) {
               var5.addSuppressed(var19);
            }

            if (conn != null) {
               conn.close();
            }

            throw var5;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var20) {
         if (var5 == null) {
            var5 = var20;
         } else if (var5 != var20) {
            var5.addSuppressed(var20);
         }

         throw var5;
      }
   }

   public static void updateEmployee(int id, String name, String position, double salary) throws SQLException {
      String sql = "UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?";
      Throwable var6 = null;
      Object var7 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, name);
               pstmt.setString(2, position);
               pstmt.setDouble(3, salary);
               pstmt.setInt(4, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var20) {
            if (var6 == null) {
               var6 = var20;
            } else if (var6 != var20) {
               var6.addSuppressed(var20);
            }

            if (conn != null) {
               conn.close();
            }

            throw var6;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var21) {
         if (var6 == null) {
            var6 = var21;
         } else if (var6 != var21) {
            var6.addSuppressed(var21);
         }

         throw var6;
      }
   }

   public static void deleteEmployee(int id) throws SQLException {
      String sql = "DELETE FROM employees WHERE id = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setInt(1, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var16) {
            if (var2 == null) {
               var2 = var16;
            } else if (var2 != var16) {
               var2.addSuppressed(var16);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var17) {
         if (var2 == null) {
            var2 = var17;
         } else if (var2 != var17) {
            var2.addSuppressed(var17);
         }

         throw var2;
      }
   }

   public static ResultSet getAllEmployees() throws SQLException {
      String sql = "SELECT * FROM employees";
      Connection conn = connect();
      return conn.createStatement().executeQuery(sql);
   }

   public static void createOrder(String customer, Map<String, Integer> books, double totalPrice) throws SQLException {
      String sql = "INSERT INTO orders(customer, bookId, quantity, totalPrice, timeCreated) VALUES(?, ?, ?, ?, datetime('now'))";
      Throwable var5 = null;
      Object var6 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               Iterator var10 = books.entrySet().iterator();

               while(true) {
                  if (!var10.hasNext()) {
                     pstmt.executeBatch();
                     break;
                  }

                  Map.Entry<String, Integer> entry = (Map.Entry)var10.next();
                  pstmt.setString(1, customer);
                  pstmt.setString(2, (String)entry.getKey());
                  pstmt.setInt(3, (Integer)entry.getValue());
                  pstmt.setDouble(4, totalPrice);
                  pstmt.addBatch();
               }
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var21) {
            if (var5 == null) {
               var5 = var21;
            } else if (var5 != var21) {
               var5.addSuppressed(var21);
            }

            if (conn != null) {
               conn.close();
            }

            throw var5;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var22) {
         if (var5 == null) {
            var5 = var22;
         } else if (var5 != var22) {
            var5.addSuppressed(var22);
         }

         throw var5;
      }
   }

   public static void updateOrder(int id, String customer, Map<String, Integer> books, double totalPrice) throws SQLException {
      String deleteSql = "DELETE FROM orders WHERE id = ?";
      String insertSql = "INSERT INTO orders(id, customer, bookId, quantity, totalPrice, timeCreated) VALUES(?, ?, ?, ?, ?, datetime('now'))";
      Throwable var7 = null;
      Object var8 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement deletePstmt = conn.prepareStatement(deleteSql);

            try {
               PreparedStatement insertPstmt = conn.prepareStatement(insertSql);

               try {
                  deletePstmt.setInt(1, id);
                  deletePstmt.executeUpdate();
                  Iterator var13 = books.entrySet().iterator();

                  while(var13.hasNext()) {
                     Map.Entry<String, Integer> entry = (Map.Entry)var13.next();
                     insertPstmt.setInt(1, id);
                     insertPstmt.setString(2, customer);
                     insertPstmt.setString(3, (String)entry.getKey());
                     insertPstmt.setInt(4, (Integer)entry.getValue());
                     insertPstmt.setDouble(5, totalPrice);
                     insertPstmt.addBatch();
                  }

                  insertPstmt.executeBatch();
               } finally {
                  if (insertPstmt != null) {
                     insertPstmt.close();
                  }

               }
            } catch (Throwable var31) {
               if (var7 == null) {
                  var7 = var31;
               } else if (var7 != var31) {
                  var7.addSuppressed(var31);
               }

               if (deletePstmt != null) {
                  deletePstmt.close();
               }

               throw var7;
            }

            if (deletePstmt != null) {
               deletePstmt.close();
            }
         } catch (Throwable var32) {
            if (var7 == null) {
               var7 = var32;
            } else if (var7 != var32) {
               var7.addSuppressed(var32);
            }

            if (conn != null) {
               conn.close();
            }

            throw var7;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var33) {
         if (var7 == null) {
            var7 = var33;
         } else if (var7 != var33) {
            var7.addSuppressed(var33);
         }

         throw var7;
      }
   }

   public static void deleteOrder(int id) throws SQLException {
      String sql = "DELETE FROM orders WHERE id = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setInt(1, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var16) {
            if (var2 == null) {
               var2 = var16;
            } else if (var2 != var16) {
               var2.addSuppressed(var16);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var17) {
         if (var2 == null) {
            var2 = var17;
         } else if (var2 != var17) {
            var2.addSuppressed(var17);
         }

         throw var2;
      }
   }

   public static ResultSet getAllOrders() throws SQLException {
      String sql = "SELECT * FROM orders";
      Connection conn = connect();
      return conn.createStatement().executeQuery(sql);
   }

   public static double getBookPrice(String bookTitle) throws SQLException {
      String sql = "SELECT price FROM books WHERE title = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         PreparedStatement var10000;
         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, bookTitle);
               ResultSet rs = pstmt.executeQuery();
               if (!rs.next()) {
                  throw new SQLException("Book not found");
               }

               rs.getDouble("price");
            } finally {
               var10000 = pstmt;
               if (pstmt != null) {
                  var10000 = pstmt;
                  pstmt.close();
               }

            }
         } catch (Throwable var17) {
            if (var2 == null) {
               var2 = var17;
            } else if (var2 != var17) {
               var2.addSuppressed(var17);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

         return (double)var10000;
      } catch (Throwable var18) {
         if (var2 == null) {
            var2 = var18;
         } else if (var2 != var18) {
            var2.addSuppressed(var18);
         }

         throw var2;
      }
   }

   public static double getBookPriceById(String bookId) throws SQLException {
      String sql = "SELECT price FROM books WHERE bookId = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         PreparedStatement var10000;
         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, bookId);
               ResultSet rs = pstmt.executeQuery();
               if (!rs.next()) {
                  throw new SQLException("Book not found");
               }

               rs.getDouble("price");
            } finally {
               var10000 = pstmt;
               if (pstmt != null) {
                  var10000 = pstmt;
                  pstmt.close();
               }

            }
         } catch (Throwable var17) {
            if (var2 == null) {
               var2 = var17;
            } else if (var2 != var17) {
               var2.addSuppressed(var17);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

         return (double)var10000;
      } catch (Throwable var18) {
         if (var2 == null) {
            var2 = var18;
         } else if (var2 != var18) {
            var2.addSuppressed(var18);
         }

         throw var2;
      }
   }

   public static String getUserRole(String username) {
      String sql = "SELECT role FROM accounts WHERE username = ?";

      try {
         Throwable var2 = null;
         Object var3 = null;

         try {
            Connection conn = connect();

            PreparedStatement var10000;
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  pstmt.setString(1, username);
                  ResultSet rs = pstmt.executeQuery();
                  if (!rs.next()) {
                     throw new SQLException("User not found");
                  }

                  rs.getString("role");
               } finally {
                  var10000 = pstmt;
                  if (pstmt != null) {
                     var10000 = pstmt;
                     pstmt.close();
                  }

               }
            } catch (Throwable var20) {
               if (var2 == null) {
                  var2 = var20;
               } else if (var2 != var20) {
                  var2.addSuppressed(var20);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var2;
            }

            if (conn != null) {
               conn.close();
            }

            return var10000;
         } catch (Throwable var21) {
            if (var2 == null) {
               var2 = var21;
            } else if (var2 != var21) {
               var2.addSuppressed(var21);
            }

            throw var2;
         }
      } catch (SQLException var22) {
         var22.printStackTrace();
         return null;
      }
   }

   public static void createToy(String id, String name, String origin, String ageLimit, double price) throws SQLException {
      String sql = "INSERT INTO toys(id, name, origin, age_limit, price) VALUES(?, ?, ?, ?, ?)";
      Throwable var7 = null;
      Object var8 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, id);
               pstmt.setString(2, name);
               pstmt.setString(3, origin);
               pstmt.setString(4, ageLimit);
               pstmt.setDouble(5, price);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var21) {
            if (var7 == null) {
               var7 = var21;
            } else if (var7 != var21) {
               var7.addSuppressed(var21);
            }

            if (conn != null) {
               conn.close();
            }

            throw var7;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var22) {
         if (var7 == null) {
            var7 = var22;
         } else if (var7 != var22) {
            var7.addSuppressed(var22);
         }

         throw var7;
      }
   }

   public static void deleteToy(String id) throws SQLException {
      String sql = "DELETE FROM toys WHERE id = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, id);
               pstmt.executeUpdate();
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var16) {
            if (var2 == null) {
               var2 = var16;
            } else if (var2 != var16) {
               var2.addSuppressed(var16);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

      } catch (Throwable var17) {
         if (var2 == null) {
            var2 = var17;
         } else if (var2 != var17) {
            var2.addSuppressed(var17);
         }

         throw var2;
      }
   }

   public static String generateUniqueToyId() throws SQLException {
      Random random = new Random();

      String toyId;
      do {
         toyId = String.format("TOY-%04d-%c", random.nextInt(10000), (char)(random.nextInt(26) + 65));
      } while(isToyIdExists(toyId));

      return toyId;
   }

   private static boolean isToyIdExists(String toyId) throws SQLException {
      String sql = "SELECT COUNT(*) FROM toys WHERE id = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         boolean var10000;
         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, toyId);
               ResultSet rs = pstmt.executeQuery();
               rs.next();
               var10000 = rs.getInt(1) > 0;
            } finally {
               if (pstmt != null) {
                  pstmt.close();
               }

            }
         } catch (Throwable var17) {
            if (var2 == null) {
               var2 = var17;
            } else if (var2 != var17) {
               var2.addSuppressed(var17);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var18) {
         if (var2 == null) {
            var2 = var18;
         } else if (var2 != var18) {
            var2.addSuppressed(var18);
         }

         throw var2;
      }
   }

   public static ResultSet getAllToys() throws SQLException {
      String sql = "SELECT * FROM toys";
      Connection conn = connect();
      return conn.createStatement().executeQuery(sql);
   }

   public static ResultSet getAllBills() throws SQLException {
      Connection connection = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
      Statement statement = connection.createStatement();
      return statement.executeQuery("SELECT * FROM bills");
   }

   public static String getBookTitleById(String bookId) throws SQLException {
      String sql = "SELECT title FROM books WHERE bookId = ?";
      Throwable var2 = null;
      Object var3 = null;

      try {
         Connection conn = connect();

         PreparedStatement var10000;
         try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            try {
               pstmt.setString(1, bookId);
               ResultSet rs = pstmt.executeQuery();
               if (!rs.next()) {
                  throw new SQLException("Book not found");
               }

               rs.getString("title");
            } finally {
               var10000 = pstmt;
               if (pstmt != null) {
                  var10000 = pstmt;
                  pstmt.close();
               }

            }
         } catch (Throwable var17) {
            if (var2 == null) {
               var2 = var17;
            } else if (var2 != var17) {
               var2.addSuppressed(var17);
            }

            if (conn != null) {
               conn.close();
            }

            throw var2;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var18) {
         if (var2 == null) {
            var2 = var18;
         } else if (var2 != var18) {
            var2.addSuppressed(var18);
         }

         throw var2;
      }
   }

   public static ResultSet getOrdersByCustomer(String customer) throws SQLException {
      Connection conn = connect();
      String query = "SELECT * FROM orders WHERE customer = ?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, customer);
      return stmt.executeQuery();
   }

   public static double getTotalRevenue() throws SQLException {
      String sql = "SELECT SUM(totalPrice) FROM orders";
      Throwable var1 = null;
      Object var2 = null;

      try {
         Connection conn = connect();

         double var10000;
         label312: {
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                     var10000 = rs.getDouble(1);
                     break label312;
                  }
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var16) {
               if (var1 == null) {
                  var1 = var16;
               } else if (var1 != var16) {
                  var1.addSuppressed(var16);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var1;
            }

            if (conn != null) {
               conn.close();
            }

            return 0.0;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var17) {
         if (var1 == null) {
            var1 = var17;
         } else if (var1 != var17) {
            var1.addSuppressed(var17);
         }

         throw var1;
      }
   }

   public static double getTotalCost() throws SQLException {
      String sql = "SELECT SUM(price * quantity) FROM books";
      Throwable var1 = null;
      Object var2 = null;

      try {
         Connection conn = connect();

         double var10000;
         label312: {
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                     var10000 = rs.getDouble(1);
                     break label312;
                  }
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var16) {
               if (var1 == null) {
                  var1 = var16;
               } else if (var1 != var16) {
                  var1.addSuppressed(var16);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var1;
            }

            if (conn != null) {
               conn.close();
            }

            return 0.0;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var17) {
         if (var1 == null) {
            var1 = var17;
         } else if (var1 != var17) {
            var1.addSuppressed(var17);
         }

         throw var1;
      }
   }

   public static double getOperationalCost() throws SQLException {
      String sql = "SELECT SUM(salary) FROM employees";
      Throwable var1 = null;
      Object var2 = null;

      try {
         Connection conn = connect();

         double var10000;
         label312: {
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                     var10000 = rs.getDouble(1);
                     break label312;
                  }
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var16) {
               if (var1 == null) {
                  var1 = var16;
               } else if (var1 != var16) {
                  var1.addSuppressed(var16);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var1;
            }

            if (conn != null) {
               conn.close();
            }

            return 0.0;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var17) {
         if (var1 == null) {
            var1 = var17;
         } else if (var1 != var17) {
            var1.addSuppressed(var17);
         }

         throw var1;
      }
   }

   public static double getBookRevenue() throws SQLException {
      String sql = "SELECT SUM(price * 1.20) FROM books";
      Throwable var1 = null;
      Object var2 = null;

      try {
         Connection conn = connect();

         double var10000;
         label312: {
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                     var10000 = rs.getDouble(1);
                     break label312;
                  }
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var16) {
               if (var1 == null) {
                  var1 = var16;
               } else if (var1 != var16) {
                  var1.addSuppressed(var16);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var1;
            }

            if (conn != null) {
               conn.close();
            }

            return 0.0;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var17) {
         if (var1 == null) {
            var1 = var17;
         } else if (var1 != var17) {
            var1.addSuppressed(var17);
         }

         throw var1;
      }
   }

   public static double getToyRevenue() throws SQLException {
      String sql = "SELECT SUM(price * 1.35) FROM toys";
      Throwable var1 = null;
      Object var2 = null;

      try {
         Connection conn = connect();

         double var10000;
         label312: {
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                     var10000 = rs.getDouble(1);
                     break label312;
                  }
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var16) {
               if (var1 == null) {
                  var1 = var16;
               } else if (var1 != var16) {
                  var1.addSuppressed(var16);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var1;
            }

            if (conn != null) {
               conn.close();
            }

            return 0.0;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var17) {
         if (var1 == null) {
            var1 = var17;
         } else if (var1 != var17) {
            var1.addSuppressed(var17);
         }

         throw var1;
      }
   }

   public static double getStationaryRevenue() throws SQLException {
      String sql = "SELECT SUM(price * 1.10) FROM stationeries";
      Throwable var1 = null;
      Object var2 = null;

      try {
         Connection conn = connect();

         double var10000;
         label312: {
            try {
               PreparedStatement pstmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = pstmt.executeQuery();
                  if (rs.next()) {
                     var10000 = rs.getDouble(1);
                     break label312;
                  }
               } finally {
                  if (pstmt != null) {
                     pstmt.close();
                  }

               }
            } catch (Throwable var16) {
               if (var1 == null) {
                  var1 = var16;
               } else if (var1 != var16) {
                  var1.addSuppressed(var16);
               }

               if (conn != null) {
                  conn.close();
               }

               throw var1;
            }

            if (conn != null) {
               conn.close();
            }

            return 0.0;
         }

         if (conn != null) {
            conn.close();
         }

         return var10000;
      } catch (Throwable var17) {
         if (var1 == null) {
            var1 = var17;
         } else if (var1 != var17) {
            var1.addSuppressed(var17);
         }

         throw var1;
      }
   }

   public static ResultSet getOrdersByCustomerAndTime(String customer, String timeCreated) throws SQLException {
      String sql = "SELECT * FROM orders WHERE customer = ? AND timeCreated = ?";
      Connection conn = connect();
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, customer);
      pstmt.setString(2, timeCreated);
      return pstmt.executeQuery();
   }

   public static ResultSet getMostRecentOrdersByCustomer(String customer) throws SQLException {
      String sql = "SELECT * FROM orders WHERE customer = ? ORDER BY timeCreated DESC";
      Connection conn = connect();
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, customer);
      return pstmt.executeQuery();
   }
}
