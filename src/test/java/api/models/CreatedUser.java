package api.models;

public record CreatedUser(
        String name,
        String job,
        String id,
        String createdAt
) {
}
