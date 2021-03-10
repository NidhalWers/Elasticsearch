package org.snp.model;

public class JoinedReference {

    private String firstRef;
    private String secondRef;

    private JoinedReference(String firstRef, String secondRef) {
        this.firstRef = firstRef;
        this.secondRef = secondRef;
    }

    public void setFirstRef(String firstRef) {
        this.firstRef = firstRef;
    }

    public void setSecondRef(String secondRef) {
        this.secondRef = secondRef;
    }

    public String getFirstRef() {
        return firstRef;
    }

    public String getSecondRef() {
        return secondRef;
    }

    @Override
    public String toString() {
        return "JoinedReference{" +
                "firstRef='" + firstRef + '\'' +
                ", secondRef='" + secondRef + '\'' +
                '}';
    }

    /**
     * Builder
     */

    public static Builder builder(){
        return new Builder();
    }

    private static class Builder{
        private String firstRef;
        private String secondRef;

        public Builder firstRef(String firstRef){
            this.firstRef=firstRef;
            return this;
        }
        public Builder secondRef(String secondRef){
            this.secondRef=secondRef;
            return this;
        }

        public JoinedReference build(){
            return new JoinedReference(this.firstRef, this.secondRef);
        }
    }
}
