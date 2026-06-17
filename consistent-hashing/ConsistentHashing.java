class HashRing {


  class RingServer {
    String serverId;
    List<String> data = new ArrayList<>();
  }

  private static TreeMap<Integer, RingServer> rings = new TreeMap<>();

  public HashRing() {

  }

  public void addServer(String serverId) {
    int hashed = hash(serverId);
    RingServer ringServer = new RingServer();
    ringServer.serverId = serverId;
    rings.put(hashed, ringServer);
  }

  public void removeServer(String serverId) {
    int hashed = hash(serverId);
    List<String> currentData = rings.get(hashed).data;
    rings.remove(hashed);
    for(String data : currentData){
      int hashedData = hash(data);
      RingServer ringServer = rings.ceilingEntry(hashedData).getValue();
      ringServer.data.add(data);
    }

  }

  public String getServer(String key) {
    int hashed = hash(key);
    RingServer ringServer = rings.ceilingEntry(hashed).getValue();
    return ringServer.serverId;
  }

  int hash(String s) {
    int sum = 0;
    for (char c : s.toCharArray()) {
      sum += c;
    }
    return sum % 1000;
  }
}