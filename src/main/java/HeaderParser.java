import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class HeaderParser {
    private short id;
    public short getId() {
        return id;
    }

    private short flags;
    public short getFlags() {
        return flags;
    }

    private short qdcount;
    public short getQdcount() {
        return qdcount;
    }

    private short ancount;
    public short getAncount() {
        return ancount;
    }

    private short arcount;
    public short getArcount() {
        return arcount;
    }

    private short nscount;
    public short getNscount() {
        return nscount;
    }

    private void ParseHeader(HeaderParser headerData, ByteBuffer requestbuffer) {
        headerData.id = requestbuffer.getShort();
        headerData.flags = ParseFlags(requestbuffer.getShort());
        headerData.qdcount = requestbuffer.getShort();
        headerData.ancount = requestbuffer.getShort();
        headerData.arcount = requestbuffer.getShort();
        headerData.nscount = requestbuffer.getShort();

        System.out.printf("%d, %d, %d, %d, %d", headerData.id, headerData.qdcount, headerData.ancount,
                headerData.arcount,
                headerData.nscount);
    }
    
    private short ParseFlags(short flags) {
        short output = (short) 0b1000_0000_0000_0000;
        short opcode = (short) ((flags>>11) & 0b1111);
        output |= (opcode <<11);//mimic the opcode sent by tester
        output |= (((flags>>8)&0b1)<<8);//mimic the Recursion desired sent by tester
        
        short rcode; 
        if (opcode == 0) {
            rcode = 0b0000; // No error
        } else {
            rcode = 0b0100 ; // Not implemented
        }

        output |= rcode;
        
        return output;
    }   
    

    public HeaderParser Parser(DatagramPacket datapacket) {

        System.out.println("into the parser");

        HeaderParser headerData = new HeaderParser();

        ByteBuffer requestbuffer = ByteBuffer.wrap(datapacket.getData());

        ParseHeader(headerData, requestbuffer);

        return headerData;

    }
}