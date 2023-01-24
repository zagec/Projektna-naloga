package com.mygdx.game.utils.db;

import org.bson.types.ObjectId;

public class RestaurantRating {
    ObjectId id;
    ObjectId user_tk;
    ObjectId restaurant_tk;
    float starRating;

    public RestaurantRating() {}

    @Override
    public String toString() {
        return "Rating{" +
                "id='" + id.toString() + '\'' +
                ", restaurantId='" + restaurant_tk.toString() + '\'' +
                ", userId='" + user_tk.toString() + '\'' +
                ", rating='" + starRating +
                '}';
    }

    public void setId(ObjectId id) { this.id = id; }
    public void setUser_tk(ObjectId id) { this.user_tk = id; }
    public void setRestaurant_tk(ObjectId id) { this.restaurant_tk = id; }
    public void setStarRating(float starRating) { this.starRating = starRating; }

    public ObjectId getId() { return this.id; }
    public ObjectId getRestaurant_tk() { return this.restaurant_tk; }
    public ObjectId getUser_tk() { return this.user_tk; }
    public float getStarRating() { return this.starRating; }

    public boolean isSameRestaurant(RestaurantRating rest){ return this.restaurant_tk.equals(rest.getRestaurant_tk());}
}
