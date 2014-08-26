package my.job.test1.hr.application;

import net.sourceforge.argparse4j.inf.Namespace;

public interface ICommand {

    public void run(Namespace ns) throws Exception;

}
