import Controller.AccountControllers.*;
import Controller.AssignmentControllers.fillASlot;
import Controller.AssignmentControllers.unfillASlot;
import Controller.AssignmentControllers.viewAssignmentByWorkSlot;
import Controller.AuthControllers.*;
import Controller.BidControllers.*;
import Controller.ProfileControllers.*;
import Controller.WorkSlotControllers.*;
import Entity.*;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import com.google.gson.Gson;

//Account handlers
class viewAllAccountsHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            viewAllAccounts viewAccount = new viewAllAccounts();
            List<Account> accounts = viewAccount.viewAllAccounts();
            String jsonResponse = gson.toJson(accounts);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class createAccountHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            createAccount createAccount = new createAccount();
            String result = createAccount.createNewAccount(
                    account.getUsername(),
                    account.getFull_name(),
                    account.getPassword(),
                    account.getP_id(),
                    account.getMax_slot());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class viewUpdateSuspendAccountHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[3]);
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            updateAccount updateAccount = new updateAccount();
            String result = updateAccount.updateOneAccount(
                    user_id,
                    account.getUsername(),
                    account.getFull_name(),
                    account.getPassword(),
                    account.getP_id(),
                    account.getMax_slot());

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[3]);
            suspendAccount suspendAccount = new suspendAccount();
            String result = suspendAccount.suspendOneAccount(user_id);

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[3]);

            viewAccount viewAccount = new viewAccount();
            Account account = viewAccount.viewOneAccount(user_id);
            String jsonResponse = gson.toJson(account);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class searchAccountHandler implements HttpHandler{
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            String substring = pathSegments[3];
            searchAccount searchAccount = new searchAccount();
            List<Account> accounts = searchAccount.searchAccountBySubString(substring);
            String jsonResponse = gson.toJson(accounts);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class adminViewUpdateAccountHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);

            adminViewAccount adminViewAccount = new adminViewAccount();
            Account account = adminViewAccount.viewOneAccount(user_id);
            String jsonResponse = gson.toJson(account);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            adminUpdateAccount adminUpdateAccount = new adminUpdateAccount();
            String result = adminUpdateAccount.updateOneAccount(
                    user_id,
                    account.getUsername(),
                    account.getFull_name(),
                    account.getPassword(),
                    account.getP_id(),
                    account.getMax_slot());

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }
}
class managerViewUpdateAccountHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);

            managerViewAccount managerViewAccount = new managerViewAccount();
            Account account = managerViewAccount.viewOneAccount(user_id);
            String jsonResponse = gson.toJson(account);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            managerUpdateAccount managerUpdateAccount = new managerUpdateAccount();
            String result = managerUpdateAccount.updateOneAccount(
                    user_id,
                    account.getUsername(),
                    account.getFull_name(),
                    account.getPassword(),
                    account.getP_id(),
                    account.getMax_slot());

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }
}
class staffViewUpdateAccountHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);

            staffViewAccount staffViewAccount = new staffViewAccount();
            Account account = staffViewAccount.viewOneAccount(user_id);
            String jsonResponse = gson.toJson(account);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            staffUpdateAccount staffUpdateAccount = new staffUpdateAccount();
            String result = staffUpdateAccount.updateOneAccount(
                    user_id,
                    account.getUsername(),
                    account.getFull_name(),
                    account.getPassword(),
                    account.getP_id(),
                    account.getMax_slot());

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }
}
class ownerViewUpdateAccountHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);

            ownerViewAccount ownerViewAccount = new ownerViewAccount();
            Account account = ownerViewAccount.viewOneAccount(user_id);
            String jsonResponse = gson.toJson(account);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int user_id = Integer.parseInt(pathSegments[4]);
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            ownerUpdateAccount ownerUpdateAccount = new ownerUpdateAccount();
            String result = ownerUpdateAccount.updateOneAccount(
                    user_id,
                    account.getUsername(),
                    account.getFull_name(),
                    account.getPassword(),
                    account.getP_id(),
                    account.getMax_slot());

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }
}
class viewAllStaffHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            viewStaff viewStaff = new viewStaff();
            List<Account> accounts = viewStaff.viewAllStaff();
            String jsonResponse = gson.toJson(accounts);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewStaffByRoleHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 5) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int p_id = Integer.parseInt(pathSegments[4]);
            viewStaffByRole viewStaffByRole = new viewStaffByRole();
            List<Object> accounts = viewStaffByRole.viewStaffByRole(p_id);
            String jsonResponse = gson.toJson(accounts);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
//Profile handlers
class viewAllProfilesHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            viewAllProfiles viewAllProfiles = new viewAllProfiles();
            List<Profile> profiles = viewAllProfiles.viewAllProfiles();
            String jsonResponse = gson.toJson(profiles);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class createProfileHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            Profile profile = gson.fromJson(jsonRequest, Profile.class);
            createProfile createProfile = new createProfile();
            String result = createProfile.createNewProfile(
                    profile.getProfile_name(),
                    profile.getProfile_desc());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class viewUpdateDeleteProfileHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int profile_id = Integer.parseInt(pathSegments[3]);
            String jsonRequest = readRequestBody(exchange);
            Profile profile = gson.fromJson(jsonRequest, Profile.class);
            updateProfile updateProfile = new updateProfile();
            String result = updateProfile.updateOneProfile(
                    profile_id,
                    profile.getProfile_name(),
                    profile.getProfile_desc());

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int profile_id = Integer.parseInt(pathSegments[3]);
            deleteProfile deleteProfile = new deleteProfile();
            String result = deleteProfile.deleteOneProfile(profile_id);

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int profile_id = Integer.parseInt(pathSegments[3]);

            viewProfile viewProfile = new viewProfile();
            Profile profile = viewProfile.viewOneProfile(profile_id);
            String jsonResponse = gson.toJson(profile);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class searchProfileHandler implements HttpHandler{
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            String substring = pathSegments[3];
            searchProfile searchProfile = new searchProfile();
            List<Profile> profiles = searchProfile.searchProfileBySubString(substring);
            String jsonResponse = gson.toJson(profiles);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
//Bid handlers
class staffViewSlotHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int staff_id = Integer.parseInt(pathSegments[3]);
            staffViewSlot staffViewSlot = new staffViewSlot();
            List<Object> bids = staffViewSlot.staffViewFinal(staff_id);
            String jsonResponse = gson.toJson(bids);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class createBidHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            String jsonRequest = readRequestBody(exchange);
            Bid bid = gson.fromJson(jsonRequest, Bid.class);
            createBid createBid = new createBid();
            String result = createBid.createNewBid(
                    bid.getStaff_id(),
                    bid.getSlot_id());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class viewPendingBidHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int staff_id = Integer.parseInt(pathSegments[3]);
            viewPendingBid viewBid = new viewPendingBid();
            List<Object> bids = viewBid.viewPendingBidsByStaffId(staff_id);
            String jsonResponse = gson.toJson(bids);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewSuccessBidHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int staff_id = Integer.parseInt(pathSegments[3]);
            viewSuccessBid viewSuccessBid = new viewSuccessBid();
            List<Object> bids = viewSuccessBid.viewSuccessBidsByStaffId(staff_id);
            String jsonResponse = gson.toJson(bids);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewRejectBidHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int staff_id = Integer.parseInt(pathSegments[3]);
            viewRejectBid viewRejectBid = new viewRejectBid();
            List<Object> bids = viewRejectBid.viewRejectBidsByStaffId(staff_id);
            String jsonResponse = gson.toJson(bids);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewUpdateDeleteBidHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int bid_id = Integer.parseInt(pathSegments[3]);
            String jsonRequest = readRequestBody(exchange);
            Bid bid = gson.fromJson(jsonRequest, Bid.class);
            updateBid updateBid = new updateBid();
            String result = updateBid.updateOneBid(
                    bid_id,
                    bid.getStaff_id(),
                    bid.getSlot_id(),
                    bid.getStatus()
            );
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int bid_id = Integer.parseInt(pathSegments[3]);
            deleteBid deleteBid = new deleteBid();
            String result = deleteBid.deleteOneBid(bid_id);

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int bid_id = Integer.parseInt(pathSegments[3]);

            viewBid viewBid = new viewBid();
            Object bid = viewBid.viewOneBid(bid_id);
            String jsonResponse = gson.toJson(bid);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class searchBidHandler implements HttpHandler{
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 6) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int staff_id = Integer.parseInt(pathSegments[3]);
            String status = pathSegments[4];
            String substring = pathSegments[5];
            searchBid searchBid = new searchBid();
            List<Object> bids = searchBid.searchBidBySubString(staff_id, status, substring);
            String jsonResponse = gson.toJson(bids);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewStaffBidHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            viewStaffBid viewStaffBid = new viewStaffBid();
            List<Object> bids = viewStaffBid.viewAllBids();
            String jsonResponse = gson.toJson(bids);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class approveBidHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int bid_id = Integer.parseInt(pathSegments[3]);
            String jsonRequest = readRequestBody(exchange);
            Bid bid = gson.fromJson(jsonRequest, Bid.class);
            approveBid approveBid = new approveBid();
            String result = approveBid.approveOneBid(
                    bid_id
            );
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class rejectBidHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int bid_id = Integer.parseInt(pathSegments[3]);
            String jsonRequest = readRequestBody(exchange);
            Bid bid = gson.fromJson(jsonRequest, Bid.class);
            rejectBid rejectBid = new rejectBid();
            String result = rejectBid.rejectOneBid(
                    bid_id
            );
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
//Workslot handlers
class createSlotHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            WorkSlot workSlot = gson.fromJson(jsonRequest, WorkSlot.class);
            createSlot createSlot = new createSlot();
            String result = createSlot.createNewSlot(
                    workSlot.getSlot_date(),
                    workSlot.getCafe_owner_id(),
                    workSlot.getShift_time());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class ownerViewSlotHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            ownerViewSlot ownerViewSlot = new ownerViewSlot();
            List<WorkSlot> workSlots = ownerViewSlot.viewAllWorkSlots();
            String jsonResponse = gson.toJson(workSlots);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class managerViewSlotHandler implements HttpHandler {
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            managerViewSlot managerViewSlot = new managerViewSlot();
            List<WorkSlot> workSlots = managerViewSlot.viewAllWorkSlots();
            String jsonResponse = gson.toJson(workSlots);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewUpdateDeleteSlotHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int slot_id = Integer.parseInt(pathSegments[3]);
            String jsonRequest = readRequestBody(exchange);
            WorkSlot workSlot = gson.fromJson(jsonRequest, WorkSlot.class);
            updateSlot updateSlot = new updateSlot();
            String result = updateSlot.updateOneSlot(
                    slot_id,
                    workSlot.getSlot_date(),
                    workSlot.getShift_time()
            );
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int slot_id = Integer.parseInt(pathSegments[3]);
            deleteSlot deleteSlot = new deleteSlot();
            String result = deleteSlot.deleteOneSlot(slot_id);

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int slot_id = Integer.parseInt(pathSegments[3]);

            viewSlot viewSlot = new viewSlot();
            WorkSlot workSlot = viewSlot.viewOneSlot(slot_id);
            String jsonResponse = gson.toJson(workSlot);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class searchSlotHandler implements HttpHandler{
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            String substring = pathSegments[3];
            searchSlot searchSlot = new searchSlot();
            List<WorkSlot> workSlots = searchSlot.searchSlotBySubString(substring);
            String jsonResponse = gson.toJson(workSlots);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
//Assignment Handler
class fillASlotHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            Assignment assignment = gson.fromJson(jsonRequest, Assignment.class);
            fillASlot fillASlot = new fillASlot();
            String result = fillASlot.fillASlot(
                    assignment.getSlot_id(),
                    assignment.getStaff_id());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class unfillASlotHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "DELETE");  // Add this line
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");  // Add this line
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int assignment_id = Integer.parseInt(pathSegments[3]);
            unfillASlot unfillASlot = new unfillASlot();
            String result = unfillASlot.unfillASlot(assignment_id);

            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
//Auth Handler
class adminLoginHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST");  // Add this line
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");  // Add this line
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            adminLogin adminLogin = new adminLogin();
            String result = adminLogin.adminLogin(
                    account.getUsername(),
                    account.getPassword(),
                    account.getP_id());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class managerLoginHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            managerLogin managerLogin = new managerLogin();
            String result = managerLogin.managerLogin(
                    account.getUsername(),
                    account.getPassword(),
                    account.getP_id());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class ownerLoginHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            ownerLogin ownerLogin = new ownerLogin();
            String result = ownerLogin.ownerLogin(
                    account.getUsername(),
                    account.getPassword(),
                    account.getP_id());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class staffLoginHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(200, -1);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String jsonRequest = readRequestBody(exchange);
            Account account = gson.fromJson(jsonRequest, Account.class);
            staffLogin staffLogin = new staffLogin();
            String result = staffLogin.staffLogin(
                    account.getUsername(),
                    account.getPassword());
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        }
    }

}
class adminLogoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            adminLogout adminLogout = new adminLogout();
            String result = adminLogout.adminLogout();
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class managerLogoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            managerLogout managerLogout = new managerLogout();
            String result = managerLogout.managerLogout();
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class ownerLogoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            ownerLogout ownerLogout = new ownerLogout();
            String result = ownerLogout.ownerLogout();
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class staffLogoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            staffLogout staffLogout = new staffLogout();
            String result = staffLogout.staffLogout();
            // Send a response if needed
            exchange.sendResponseHeaders(201, 0); // 201 Created
            OutputStream os = exchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}

//Extra
class getUserIdByUsernameHandler implements HttpHandler{
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            String username = pathSegments[3];
            getUserIdByUsername getUserIdByUsername = new getUserIdByUsername();
            int user_id = getUserIdByUsername.getUserIdByUsername(username);
            // Convert the user_id to bytes or string before writing to the output stream
            String response = String.valueOf(user_id);
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
class viewAssignmentByWorkSlotHandler implements HttpHandler{
    private final Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            if (pathSegments.length < 4) {
                // The URL path is not in the expected format, respond with an error
                exchange.sendResponseHeaders(400, 0); // Bad Request
                exchange.getResponseBody().close();
                return;
            }
            int slot_id = Integer.parseInt(pathSegments[3]);
            viewAssignmentByWorkSlot viewAssignmentByWorkSlot = new viewAssignmentByWorkSlot();
            List<Object> assignments = viewAssignmentByWorkSlot.viewAssignmentByWorkSlot(slot_id);

            // Convert the list of assignments to JSON
            String jsonResponse = gson.toJson(assignments);

            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(405, -1); // Method not allowed
        }
    }
}
public class Main {
    public static void main(String[] args) throws IOException {
//        viewAccount viewAccount = new viewAccount();
//        viewAccount.getAllAccounts();
        int port = 8080; // Choose a suitable port

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        //Account APIs
        server.createContext("/api/accounts", new viewAllAccountsHandler());
        server.createContext("/api/register", new createAccountHandler());
        server.createContext("/api/account", new viewUpdateSuspendAccountHandler());
        server.createContext("/api/searchAccount", new searchAccountHandler());
        server.createContext("/api/admin/account", new adminViewUpdateAccountHandler());
        server.createContext("/api/manager/account", new managerViewUpdateAccountHandler());
        server.createContext("/api/staff/account", new staffViewUpdateAccountHandler());
        server.createContext("/api/owner/account", new ownerViewUpdateAccountHandler());
        server.createContext("/api/accounts/staff", new viewAllStaffHandler());
        server.createContext("/api/accounts/staffRole", new viewStaffByRoleHandler());
        //Profile APIs
        server.createContext("/api/profiles", new viewAllProfilesHandler());
        server.createContext("/api/createProfile", new createProfileHandler());
        server.createContext("/api/profile", new viewUpdateDeleteProfileHandler());
        server.createContext("/api/searchProfile", new searchProfileHandler());
        //Bid APIs
        server.createContext("/api/staffViewSlot", new staffViewSlotHandler());
        server.createContext("/api/createBid", new createBidHandler());
        server.createContext("/api/staffPendingBids", new viewPendingBidHandler());
        server.createContext("/api/staffSuccessBids", new viewSuccessBidHandler());
        server.createContext("/api/staffRejectBids", new viewRejectBidHandler());
        server.createContext("/api/bid", new viewUpdateDeleteBidHandler());
        server.createContext("/api/searchBid", new searchBidHandler());
        server.createContext("/api/bids", new viewStaffBidHandler());
        server.createContext("/api/approveBid", new approveBidHandler());
        server.createContext("/api/rejectBid", new rejectBidHandler());
        //Workslot APIs
        server.createContext("/api/createSlot", new createSlotHandler());
        server.createContext("/api/ownerWorkSlots", new ownerViewSlotHandler());
        server.createContext("/api/managerWorkSlots", new managerViewSlotHandler());
        server.createContext("/api/workSlot", new viewUpdateDeleteSlotHandler());
        server.createContext("/api/searchSlot", new searchSlotHandler());
        //Assignment APIs
        server.createContext("/api/fillASlot", new fillASlotHandler());
        server.createContext("/api/unfillASlot", new unfillASlotHandler());
        server.createContext("/api/assignmentsBySlot", new viewAssignmentByWorkSlotHandler());
        //Auth APIs
        server.createContext("/api/adminLogin", new adminLoginHandler());
        server.createContext("/api/adminLogout", new adminLogoutHandler());
        server.createContext("/api/managerLogin", new managerLoginHandler());
        server.createContext("/api/managerLogout", new managerLogoutHandler());
        server.createContext("/api/ownerLogin", new ownerLoginHandler());
        server.createContext("/api/ownerLogout", new ownerLogoutHandler());
        server.createContext("/api/staffLogin", new staffLoginHandler());
        server.createContext("/api/staffLogout", new staffLogoutHandler());

        //Extra
        server.createContext("/api/getUserId", new getUserIdByUsernameHandler());
        server.setExecutor(null); // Use the default executor

        server.start();

        System.out.println("Server started on port " + port);
    }
}