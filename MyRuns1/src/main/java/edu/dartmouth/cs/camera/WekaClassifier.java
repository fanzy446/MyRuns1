package edu.dartmouth.cs.camera;

/**
 * Created by oubai on 5/3/16.
 */

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.Na10ef030(i);
        return p;
    }
    static double Na10ef030(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 47.21931) {
            p = WekaClassifier.N79f5e4b61(i);
        } else if (((Double) i[0]).doubleValue() > 47.21931) {
            p = WekaClassifier.N17eaf5177(i);
        }
        return p;
    }
    static double N79f5e4b61(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 30.851032) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 30.851032) {
            p = WekaClassifier.N3160fc872(i);
        }
        return p;
    }
    static double N3160fc872(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 1.069038) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() > 1.069038) {
            p = WekaClassifier.N79f48ce3(i);
        }
        return p;
    }
    static double N79f48ce3(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() <= 1.003614) {
            p = WekaClassifier.Nafad7c94(i);
        } else if (((Double) i[8]).doubleValue() > 1.003614) {
            p = 0;
        }
        return p;
    }
    static double Nafad7c94(Object []i) {
        double p = Double.NaN;
        if (i[24] == null) {
            p = 1;
        } else if (((Double) i[24]).doubleValue() <= 0.2832) {
            p = 1;
        } else if (((Double) i[24]).doubleValue() > 0.2832) {
            p = WekaClassifier.N5c7c52745(i);
        }
        return p;
    }
    static double N5c7c52745(Object []i) {
        double p = Double.NaN;
        if (i[19] == null) {
            p = 1;
        } else if (((Double) i[19]).doubleValue() <= 0.185314) {
            p = 1;
        } else if (((Double) i[19]).doubleValue() > 0.185314) {
            p = WekaClassifier.N6ace19b86(i);
        }
        return p;
    }
    static double N6ace19b86(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 0.484833) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 0.484833) {
            p = 0;
        }
        return p;
    }
    static double N17eaf5177(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 417.179036) {
            p = WekaClassifier.N4b723cbc8(i);
        } else if (((Double) i[0]).doubleValue() > 417.179036) {
            p = WekaClassifier.N19a2b39b21(i);
        }
        return p;
    }
    static double N4b723cbc8(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 135.027292) {
            p = WekaClassifier.N752328549(i);
        } else if (((Double) i[0]).doubleValue() > 135.027292) {
            p = WekaClassifier.N1197ac7616(i);
        }
        return p;
    }
    static double N752328549(Object []i) {
        double p = Double.NaN;
        if (i[23] == null) {
            p = 1;
        } else if (((Double) i[23]).doubleValue() <= 2.941359) {
            p = WekaClassifier.N6f3aec9110(i);
        } else if (((Double) i[23]).doubleValue() > 2.941359) {
            p = 0;
        }
        return p;
    }
    static double N6f3aec9110(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 75.031963) {
            p = WekaClassifier.N2fb8fe2c11(i);
        } else if (((Double) i[0]).doubleValue() > 75.031963) {
            p = WekaClassifier.N211e7e0314(i);
        }
        return p;
    }
    static double N2fb8fe2c11(Object []i) {
        double p = Double.NaN;
        if (i[25] == null) {
            p = 1;
        } else if (((Double) i[25]).doubleValue() <= 0.360927) {
            p = 1;
        } else if (((Double) i[25]).doubleValue() > 0.360927) {
            p = WekaClassifier.N75f5b0bb12(i);
        }
        return p;
    }
    static double N75f5b0bb12(Object []i) {
        double p = Double.NaN;
        if (i[31] == null) {
            p = 0;
        } else if (((Double) i[31]).doubleValue() <= 0.40125) {
            p = 0;
        } else if (((Double) i[31]).doubleValue() > 0.40125) {
            p = WekaClassifier.N197847d913(i);
        }
        return p;
    }
    static double N197847d913(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 11.332981) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 11.332981) {
            p = 0;
        }
        return p;
    }
    static double N211e7e0314(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 4.033774) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() > 4.033774) {
            p = WekaClassifier.N49bfad9215(i);
        }
        return p;
    }
    static double N49bfad9215(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 10.794795) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() > 10.794795) {
            p = 1;
        }
        return p;
    }
    static double N1197ac7616(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 351.956241) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 351.956241) {
            p = WekaClassifier.N4a0a771217(i);
        }
        return p;
    }
    static double N4a0a771217(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 12.981809) {
            p = WekaClassifier.N1836f63618(i);
        } else if (((Double) i[64]).doubleValue() > 12.981809) {
            p = 1;
        }
        return p;
    }
    static double N1836f63618(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 375.065067) {
            p = WekaClassifier.N2cd10b9f19(i);
        } else if (((Double) i[0]).doubleValue() > 375.065067) {
            p = 2;
        }
        return p;
    }
    static double N2cd10b9f19(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 2;
        } else if (((Double) i[13]).doubleValue() <= 2.627167) {
            p = WekaClassifier.N9f66f20(i);
        } else if (((Double) i[13]).doubleValue() > 2.627167) {
            p = 1;
        }
        return p;
    }
    static double N9f66f20(Object []i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() <= 4.501253) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() > 4.501253) {
            p = 2;
        }
        return p;
    }
    static double N19a2b39b21(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 492.814021) {
            p = WekaClassifier.N360a61df22(i);
        } else if (((Double) i[0]).doubleValue() > 492.814021) {
            p = 2;
        }
        return p;
    }
    static double N360a61df22(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 2;
        } else if (((Double) i[10]).doubleValue() <= 12.077438) {
            p = WekaClassifier.N12a3097223(i);
        } else if (((Double) i[10]).doubleValue() > 12.077438) {
            p = 1;
        }
        return p;
    }
    static double N12a3097223(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 87.20657) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() > 87.20657) {
            p = WekaClassifier.N5dd62fc724(i);
        }
        return p;
    }
    static double N5dd62fc724(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 2;
        } else if (((Double) i[11]).doubleValue() <= 4.753133) {
            p = 2;
        } else if (((Double) i[11]).doubleValue() > 4.753133) {
            p = 1;
        }
        return p;
    }
}

