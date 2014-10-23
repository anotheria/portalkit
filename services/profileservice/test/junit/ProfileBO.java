import net.anotheria.portalkit.services.profileservice.Profile;

/**
 * @author asamoilich.
 */
public class ProfileBO extends Profile {

    private static final long serialVersionUID = 687896561152349871L;

    public ProfileBO() {
    }

    private int age;
    private int height;
    private int weight;
    private String name;

    public ProfileBO(String id) {
        super(id);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProfileBO{" +
                "age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", name='" + name + '\'' +
                '}';
    }
}

