package test.e_commerceapp;

public class ItemData {
    public String name, desc, image, id;
    int price;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public ItemData() {
    }

    public ItemData(String name, String desc, int price, String image, String id) {

        this.name = name;

        this.desc = desc;
        this.price = price;
        this.image = image;
        this.id = id;
    }
}
