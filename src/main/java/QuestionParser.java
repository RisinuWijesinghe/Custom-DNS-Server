public class QuestionParser {

    private String[] domainNames;

    public String[] getDomainNames() {
        return domainNames;
    }

    public String[] Parser(short qdcount, byte[] questions) {
        domainNames = new String[qdcount];
        int index = 0;
        for (int i = 0; i < qdcount; i++) {
            StringBuilder domainName = new StringBuilder();
            while (questions[index] != 0) {
                int labellength = questions[index++];
                for (int j = 0; j < labellength; j++) {
                    domainName.append((char) questions[index++]);
                }
                if (questions[index] != 0) {
                    domainName.append(".");
                }
            }
            System.out.println(domainName.toString());
            domainNames[i] = domainName.toString();
            index++;

        }
        return domainNames;
    }
}
