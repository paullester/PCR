// TODO: figure out the deal with classes
// TODO: figure out the deal with iteration

public static void main(String[] args) {

    Configuration conf = new Configuration();
    Job job = conf.getInstance(conf, "adsorpt");
    job.setJarByClass(Adsorption.class);
    job.setMapperClass(Map.class);
    job.setCombinerClass(Reduce.class);
    job.setReducerClass(Reduce.class);
    job.setOutputKeyClass(Node.class);
    job.setOutputValueClass()
}