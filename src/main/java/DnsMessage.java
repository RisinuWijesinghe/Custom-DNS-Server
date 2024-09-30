import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class DnsMessage {
    private short id;
    private short flags;
    private short qdcount;
    private short ancount = 1;
    private short nscount;
    private short arcount;
    private byte[] questions;
    private String[] domainNames;

    public DnsMessage(HeaderParser headerData, byte[] questions, String[] domainNames) {
        this.id = headerData.getId();
        this.flags = headerData.getFlags();
        this.qdcount = headerData.getQdcount();
        // this.ancount = headerData.getAncount();
        this.nscount = headerData.getNscount();
        this.arcount = headerData.getArcount();
        this.questions = questions;
        this.domainNames = domainNames;
    }

    private ByteBuffer writeHeader(ByteBuffer byteBuffer) {
        byteBuffer.putShort(id);
        byteBuffer.putShort(flags);
        byteBuffer.putShort(qdcount);
        byteBuffer.putShort(ancount);
        byteBuffer.putShort(nscount);
        byteBuffer.putShort(arcount);

        return byteBuffer;

    }

    private ByteBuffer writeQuestion(ByteBuffer byteBuffer) {
        byteBuffer.put(questions);
        return byteBuffer;
    }

    private ByteBuffer writeAnswer(ByteBuffer byteBuffer) {
        for (String domainName : domainNames) {
            byteBuffer.put(encodeDomain(domainName));
            byteBuffer.putShort((short) 1);
            byteBuffer.putShort((short) 1);
            byteBuffer.putInt(60);
            byteBuffer.putShort((short) 4);
            byte[] ipAddressArray = new byte[] { 127, 0, 0, 1 };
            byteBuffer.put(ipAddressArray);
        }

        return byteBuffer;

    }

    private byte[] encodeDomain(String domainName) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (String label : domainName.split("\\.")) {
            output.write(label.length());
            output.writeBytes(label.getBytes());
        }
        output.write(0);
        return output.toByteArray();
    }

    public byte[] array() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        writeHeader(byteBuffer);
        writeQuestion(byteBuffer);
        writeAnswer(byteBuffer);

        return byteBuffer.array();
    }

}