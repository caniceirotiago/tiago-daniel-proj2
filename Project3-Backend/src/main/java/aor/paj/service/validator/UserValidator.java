/**Código Daniel*/

package aor.paj.service.validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.*;

import aor.paj.dto.Task;
import aor.paj.dto.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

@ApplicationScoped
public class UserValidator {

    public UserValidator() {

    }

    /**Validation*/
    private boolean validateUsername(String username) {
        return username != null && username.length() >= 2 && username.length() <= 20;
    }

    private boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.indexOf('@') < email.lastIndexOf('.');
    }

    private boolean validatePhone(String phone) {
        if (phone == null) return false;
        // Verifica o comprimento do número de telefone limpo.
        if (phone.length() < 9 || phone.length() > 20) return false;
        // Se passou por todas as verificações, o número é válido.
        return true;
    }
    private boolean validateName(String firstName, String lastName) {
        if (firstName == null || lastName == null) return false;
        return firstName.length() >= 3 && firstName.length() <= 25 &&
                lastName.length() >= 3 && lastName.length() <= 25;
    }

    private boolean validatePhotoURL(String photoURL) {
        return photoURL != null && photoURL.length() >= 3 && photoURL.length() <= 500;
    }
    public boolean validatePassword(String password) {
        // Verifica se a senha não é nula e se tem pelo menos 6 caracteres
        return password != null && password.length() >= 6;
    }
    public boolean validateUserOnRegistration(User user) {
        return validateUsername(user.getUsername()) &&
                validatePassword(user.getPassword()) &&
                validateEmail(user.getEmail()) &&
                validatePhone(user.getPhoneNumber()) &&
                validateName(user.getFirstName(), user.getLastName()) &&
                validatePhotoURL(user.getPhotoURL());
    }
    public boolean validateUserOnEdit(User user) {
        return validateEmail(user.getEmail()) &&
                validatePhone(user.getPhoneNumber()) &&
                validateName(user.getFirstName(), user.getLastName()) &&
                validatePhotoURL(user.getPhotoURL());
    }
}