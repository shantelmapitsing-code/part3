package com.mycompany.part1;

// LOGIN CLASS
class Login{
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // CONSTRUCTOR
    public Login(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // USERNAME VALIDATION
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // PASSWORD VALIDATION
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) return false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    // FIXED LOGIN METHOD (checks BOTH username and password)
    public boolean loginUser(String userName, String password) {
        return this.username.equals(userName) && this.password.equals(password);
    }

    // DISPLAY USER INFO
    public String getUserLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName;
        } else {
            return "Username or password incorrect";
        }
    }

    // GETTERS
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}