package raidsunlimited.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import raidsunlimited.converters.GameCharacterConverter;
import raidsunlimited.models.GameCharacter;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "users")
public class User {
    private String userId;
    private String displayName;
    private String email;
    private List<GameCharacter> charactersList;
    private String logs;

    @DynamoDBHashKey(attributeName = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "displayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "charactersList")
    @DynamoDBTypeConverted(converter = GameCharacterConverter.class)
    public List<GameCharacter> getCharactersList() {
        return charactersList;
    }

    public void setCharactersList(List<GameCharacter> charactersList) {
        this.charactersList = charactersList;
    }

    @DynamoDBAttribute(attributeName = "logs")
    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(displayName, user.displayName) &&
                Objects.equals(email, user.email) && Objects.equals(charactersList, user.charactersList) &&
                Objects.equals(logs, user.logs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, email, charactersList, logs);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", gameCharacterList=" + charactersList +
                ", logs='" + logs + '\'' +
                '}';
    }
}
