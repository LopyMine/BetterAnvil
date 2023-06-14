package net.lopymine.betteranvil.resourcepacks.custommodeldata;

public class CMDItem {

    private String item;
    private String resourcePack;

    private String serverResourcePack;
    private int id;

    public CMDItem(String item, int id){
        this.item = item;
        this.id = id;
        resourcePack = null;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public void setServerResourcePack(String serverResourcePack) {
        this.serverResourcePack = serverResourcePack;
    }

    public String getServerResourcePack() {
        return serverResourcePack;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(!(obj instanceof CMDItem item)) return false;
        if(item.getResourcePack() != null){
            if(item.getResourcePack().equals(this.getResourcePack()) && item.getItem().equals(this.getItem()) && item.getId() == this.getId()) return true;
        } else {
            if(item.getItem().equals(this.getItem()) && item.getId() == this.getId()) return true;
        }

        return super.equals(obj);
    }
    

}
