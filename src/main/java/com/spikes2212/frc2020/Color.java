/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.spikes2212.frc2020;

import java.util.Objects;

import edu.wpi.first.wpiutil.math.MathUtil;

public enum Color {
    /*
     * FIRST Colors
     */

    /**
     * #1560BD.
     */
     kDenim(0.0823529412, 0.376470589, 0.7411764706),

    /**
     * #0066B3.
     */
     kFirstBlue(0.0, 0.4, 0.7019607844),

    /**
     * #ED1C24.
     */
     kFirstRed(0.9294117648, 0.1098039216, 0.1411764706),

    /*
     * Standard Colors
     */

    /**
     * #F0F8FF.
     */
     kAliceBlue(0.9411765f, 0.972549f, 1.0f),

    /**
     * #FAEBD7.
     */
     kAntiqueWhite(0.98039216f, 0.92156863f, 0.84313726f),

    /**
     * #00FFFF.
     */
     kAqua(0.0f, 1.0f, 1.0f),

    /**
     * #7FFFD4.
     */
     kAquamarine(0.49803922f, 1.0f, 0.83137256f),

    /**
     * #F0FFFF.
     */
     kAzure(0.9411765f, 1.0f, 1.0f),

    /**
     * #F5F5DC.
     */
     kBeige(0.9607843f, 0.9607843f, 0.8627451f),

    /**
     * #FFE4C4.
     */
     kBisque(1.0f, 0.89411765f, 0.76862746f),

    /**
     * #000000.
     */
     kBlack(0.0f, 0.0f, 0.0f),

    /**
     * #FFEBCD.
     */
     kBlanchedAlmond(1.0f, 0.92156863f, 0.8039216f),

    /**
     * #0000FF.
     */
     kBlue(0.0f, 0.0f, 1.0f),

    /**
     * #8A2BE2.
     */
     kBlueViolet(0.5411765f, 0.16862746f, 0.8862745f),

    /**
     * #A52A2A.
     */
     kBrown(0.64705884f, 0.16470589f, 0.16470589f),

    /**
     * #DEB887.
     */
     kBurlywood(0.87058824f, 0.72156864f, 0.5294118f),

    /**
     * #5F9EA0.
     */
     kCadetBlue(0.37254903f, 0.61960787f, 0.627451f),

    /**
     * #7FFF00.
     */
     kChartreuse(0.49803922f, 1.0f, 0.0f),

    /**
     * #D2691E.
     */
     kChocolate(0.8235294f, 0.4117647f, 0.11764706f),

    /**
     * #FF7F50.
     */
     kCoral(1.0f, 0.49803922f, 0.3137255f),

    /**
     * #6495ED.
     */
     kCornflowerBlue(0.39215687f, 0.58431375f, 0.92941177f),

    /**
     * #FFF8DC.
     */
     kCornsilk(1.0f, 0.972549f, 0.8627451f),

    /**
     * #DC143C.
     */
     kCrimson(0.8627451f, 0.078431375f, 0.23529412f),

    /**
     * #00FFFF.
     */
     kCyan(0.0f, 1.0f, 1.0f),

    /**
     * #00008B.
     */
     kDarkBlue(0.0f, 0.0f, 0.54509807f),

    /**
     * #008B8B.
     */
     kDarkCyan(0.0f, 0.54509807f, 0.54509807f),

    /**
     * #B8860B.
     */
     kDarkGoldenrod(0.72156864f, 0.5254902f, 0.043137256f),

    /**
     * #A9A9A9.
     */
     kDarkGray(0.6627451f, 0.6627451f, 0.6627451f),

    /**
     * #006400.
     */
     kDarkGreen(0.0f, 0.39215687f, 0.0f),

    /**
     * #BDB76B.
     */
     kDarkKhaki(0.7411765f, 0.7176471f, 0.41960785f),

    /**
     * #8B008B.
     */
     kDarkMagenta(0.54509807f, 0.0f, 0.54509807f),

    /**
     * #556B2F.
     */
     kDarkOliveGreen(0.33333334f, 0.41960785f, 0.18431373f),

    /**
     * #FF8C00.
     */
     kDarkOrange(1.0f, 0.54901963f, 0.0f),

    /**
     * #9932CC.
     */
     kDarkOrchid(0.6f, 0.19607843f, 0.8f),

    /**
     * #8B0000.
     */
     kDarkRed(0.54509807f, 0.0f, 0.0f),

    /**
     * #E9967A.
     */
     kDarkSalmon(0.9137255f, 0.5882353f, 0.47843137f),

    /**
     * #8FBC8F.
     */
     kDarkSeaGreen(0.56078434f, 0.7372549f, 0.56078434f),

    /**
     * #483D8B.
     */
     kDarkSlateBlue(0.28235295f, 0.23921569f, 0.54509807f),

    /**
     * #2F4F4F.
     */
     kDarkSlateGray(0.18431373f, 0.30980393f, 0.30980393f),

    /**
     * #00CED1.
     */
     kDarkTurquoise(0.0f, 0.80784315f, 0.81960785f),

    /**
     * #9400D3.
     */
     kDarkViolet(0.5803922f, 0.0f, 0.827451f),

    /**
     * #FF1493.
     */
     kDeepPink(1.0f, 0.078431375f, 0.5764706f),

    /**
     * #00BFFF.
     */
     kDeepSkyBlue(0.0f, 0.7490196f, 1.0f),

    /**
     * #696969.
     */
     kDimGray(0.4117647f, 0.4117647f, 0.4117647f),

    /**
     * #1E90FF.
     */
     kDodgerBlue(0.11764706f, 0.5647059f, 1.0f),

    /**
     * #B22222.
     */
     kFirebrick(0.69803923f, 0.13333334f, 0.13333334f),

    /**
     * #FFFAF0.
     */
     kFloralWhite(1.0f, 0.98039216f, 0.9411765f),

    /**
     * #228B22.
     */
     kForestGreen(0.13333334f, 0.54509807f, 0.13333334f),

    /**
     * #FF00FF.
     */
     kFuchsia(1.0f, 0.0f, 1.0f),

    /**
     * #DCDCDC.
     */
     kGainsboro(0.8627451f, 0.8627451f, 0.8627451f),

    /**
     * #F8F8FF.
     */
     kGhostWhite(0.972549f, 0.972549f, 1.0f),

    /**
     * #FFD700.
     */
     kGold(1.0f, 0.84313726f, 0.0f),

    /**
     * #DAA520.
     */
     kGoldenrod(0.85490197f, 0.64705884f, 0.1254902f),

    /**
     * #808080.
     */
     kGray(0.5019608f, 0.5019608f, 0.5019608f),

    /**
     * #008000.
     */
     kGreen(0.0f, 0.5019608f, 0.0f),

    /**
     * #ADFF2F.
     */
     kGreenYellow(0.6784314f, 1.0f, 0.18431373f),

    /**
     * #F0FFF0.
     */
     kHoneydew(0.9411765f, 1.0f, 0.9411765f),

    /**
     * #FF69B4.
     */
     kHotPink(1.0f, 0.4117647f, 0.7058824f),

    /**
     * #CD5C5C.
     */
     kIndianRed(0.8039216f, 0.36078432f, 0.36078432f),

    /**
     * #4B0082.
     */
     kIndigo(0.29411766f, 0.0f, 0.50980395f),

    /**
     * #FFFFF0.
     */
     kIvory(1.0f, 1.0f, 0.9411765f),

    /**
     * #F0E68C.
     */
     kKhaki(0.9411765f, 0.9019608f, 0.54901963f),

    /**
     * #E6E6FA.
     */
     kLavender(0.9019608f, 0.9019608f, 0.98039216f),

    /**
     * #FFF0F5.
     */
     kLavenderBlush(1.0f, 0.9411765f, 0.9607843f),

    /**
     * #7CFC00.
     */
     kLawnGreen(0.4862745f, 0.9882353f, 0.0f),

    /**
     * #FFFACD.
     */
     kLemonChiffon(1.0f, 0.98039216f, 0.8039216f),

    /**
     * #ADD8E6.
     */
     kLightBlue(0.6784314f, 0.84705883f, 0.9019608f),

    /**
     * #F08080.
     */
     kLightCoral(0.9411765f, 0.5019608f, 0.5019608f),

    /**
     * #E0FFFF.
     */
     kLightCyan(0.8784314f, 1.0f, 1.0f),

    /**
     * #FAFAD2.
     */
     kLightGoldenrodYellow(0.98039216f, 0.98039216f, 0.8235294f),

    /**
     * #D3D3D3.
     */
     kLightGray(0.827451f, 0.827451f, 0.827451f),

    /**
     * #90EE90.
     */
     kLightGreen(0.5647059f, 0.93333334f, 0.5647059f),

    /**
     * #FFB6C1.
     */
     kLightPink(1.0f, 0.7137255f, 0.75686276f),

    /**
     * #FFA07A.
     */
     kLightSalmon(1.0f, 0.627451f, 0.47843137f),

    /**
     * #20B2AA.
     */
     kLightSeagGeen(0.1254902f, 0.69803923f, 0.6666667f),

    /**
     * #87CEFA.
     */
     kLightSkyBlue(0.5294118f, 0.80784315f, 0.98039216f),

    /**
     * #778899.
     */
     kLightSlateGray(0.46666667f, 0.53333336f, 0.6f),

    /**
     * #B0C4DE.
     */
     kLightSteellue(0.6901961f, 0.76862746f, 0.87058824f),

    /**
     * #FFFFE0.
     */
     kLightYellow(1.0f, 1.0f, 0.8784314f),

    /**
     * #00FF00.
     */
     kLime(0.0f, 1.0f, 0.0f),

    /**
     * #32CD32.
     */
     kLimeGreen(0.19607843f, 0.8039216f, 0.19607843f),

    /**
     * #FAF0E6.
     */
     kLinen(0.98039216f, 0.9411765f, 0.9019608f),

    /**
     * #FF00FF.
     */
     kMagenta(1.0f, 0.0f, 1.0f),

    /**
     * #800000.
     */
     kMaroon(0.5019608f, 0.0f, 0.0f),

    /**
     * #66CDAA.
     */
     kMediumAquamarine(0.4f, 0.8039216f, 0.6666667f),

    /**
     * #0000CD.
     */
     kMediumBlue(0.0f, 0.0f, 0.8039216f),

    /**
     * #BA55D3.
     */
     kMediumOrchid(0.7294118f, 0.33333334f, 0.827451f),

    /**
     * #9370DB.
     */
     kMediumPurple(0.5764706f, 0.4392157f, 0.85882354f),

    /**
     * #3CB371.
     */
     kMediumSeaGreen(0.23529412f, 0.7019608f, 0.44313726f),

    /**
     * #7B68EE.
     */
     kMediumSlateBlue(0.48235294f, 0.40784314f, 0.93333334f),

    /**
     * #00FA9A.
     */
     kMediumSpringGreen(0.0f, 0.98039216f, 0.6039216f),

    /**
     * #48D1CC.
     */
     kMediumTurquoise(0.28235295f, 0.81960785f, 0.8f),

    /**
     * #C71585.
     */
     kMediumVioletRed(0.78039217f, 0.08235294f, 0.52156866f),

    /**
     * #191970.
     */
     kMidnightBlue(0.09803922f, 0.09803922f, 0.4392157f),

    /**
     * #F5FFFA.
     */
     kMintcream(0.9607843f, 1.0f, 0.98039216f),

    /**
     * #FFE4E1.
     */
     kMistyRose(1.0f, 0.89411765f, 0.88235295f),

    /**
     * #FFE4B5.
     */
     kMoccasin(1.0f, 0.89411765f, 0.70980394f),

    /**
     * #FFDEAD.
     */
     kNavajoWhite(1.0f, 0.87058824f, 0.6784314f),

    /**
     * #000080.
     */
     kNavy(0.0f, 0.0f, 0.5019608f),

    /**
     * #FDF5E6.
     */
     kOldLace(0.99215686f, 0.9607843f, 0.9019608f),

    /**
     * #808000.
     */
     kOlive(0.5019608f, 0.5019608f, 0.0f),

    /**
     * #6B8E23.
     */
     kOliveDrab(0.41960785f, 0.5568628f, 0.13725491f),

    /**
     * #FFA500.
     */
     kOrange(1.0f, 0.64705884f, 0.0f),

    /**
     * #FF4500.
     */
     kOrangeRed(1.0f, 0.27058825f, 0.0f),

    /**
     * #DA70D6.
     */
     kOrchid(0.85490197f, 0.4392157f, 0.8392157f),

    /**
     * #EEE8AA.
     */
     kPaleGoldenrod(0.93333334f, 0.9098039f, 0.6666667f),

    /**
     * #98FB98.
     */
     kPaleGreen(0.59607846f, 0.9843137f, 0.59607846f),

    /**
     * #AFEEEE.
     */
     kPaleTurquoise(0.6862745f, 0.93333334f, 0.93333334f),

    /**
     * #DB7093.
     */
     kPaleVioletRed(0.85882354f, 0.4392157f, 0.5764706f),

    /**
     * #FFEFD5.
     */
     kPapayaWhip(1.0f, 0.9372549f, 0.8352941f),

    /**
     * #FFDAB9.
     */
     kPeachPuff(1.0f, 0.85490197f, 0.7254902f),

    /**
     * #CD853F.
     */
     kPeru(0.8039216f, 0.52156866f, 0.24705882f),

    /**
     * #FFC0CB.
     */
     kPink(1.0f, 0.7529412f, 0.79607844f),

    /**
     * #DDA0DD.
     */
     kPlum(0.8666667f, 0.627451f, 0.8666667f),

    /**
     * #B0E0E6.
     */
     kPowderBlue(0.6901961f, 0.8784314f, 0.9019608f),

    /**
     * #800080.
     */
     kPurple(0.5019608f, 0.0f, 0.5019608f),

    /**
     * #FF0000.
     */
     kRed(1.0f, 0.0f, 0.0f),

    /**
     * #BC8F8F.
     */
     kRosyBrown(0.7372549f, 0.56078434f, 0.56078434f),

    /**
     * #4169E1.
     */
     kRoyalBlue(0.25490198f, 0.4117647f, 0.88235295f),

    /**
     * #8B4513.
     */
     kSaddleBrown(0.54509807f, 0.27058825f, 0.07450981f),

    /**
     * #FA8072.
     */
     kSalmon(0.98039216f, 0.5019608f, 0.44705883f),

    /**
     * #F4A460.
     */
     kSandyBrown(0.95686275f, 0.6431373f, 0.3764706f),

    /**
     * #2E8B57.
     */
     kSeaGreen(0.18039216f, 0.54509807f, 0.34117648f),

    /**
     * #FFF5EE.
     */
     kSeashell(1.0f, 0.9607843f, 0.93333334f),

    /**
     * #A0522D.
     */
     kSienna(0.627451f, 0.32156864f, 0.1764706f),

    /**
     * #C0C0C0.
     */
     kSilver(0.7529412f, 0.7529412f, 0.7529412f),

    /**
     * #87CEEB.
     */
     kSkyBlue(0.5294118f, 0.80784315f, 0.92156863f),

    /**
     * #6A5ACD.
     */
     kSlateBlue(0.41568628f, 0.3529412f, 0.8039216f),

    /**
     * #708090.
     */
     kSlateGray(0.4392157f, 0.5019608f, 0.5647059f),

    /**
     * #FFFAFA.
     */
     kSnow(1.0f, 0.98039216f, 0.98039216f),

    /**
     * #00FF7F.
     */
     kSpringGreen(0.0f, 1.0f, 0.49803922f),

    /**
     * #4682B4.
     */
     kSteelBlue(0.27450982f, 0.50980395f, 0.7058824f),

    /**
     * #D2B48C.
     */
     kTan(0.8235294f, 0.7058824f, 0.54901963f),

    /**
     * #008080.
     */
     kTeal(0.0f, 0.5019608f, 0.5019608f),

    /**
     * #D8BFD8.
     */
     kThistle(0.84705883f, 0.7490196f, 0.84705883f),

    /**
     * #FF6347.
     */
     kTomato(1.0f, 0.3882353f, 0.2784314f),

    /**
     * #40E0D0.
     */
     kTurquoise(0.2509804f, 0.8784314f, 0.8156863f),

    /**
     * #EE82EE.
     */
     kViolet(0.93333334f, 0.50980395f, 0.93333334f),

    /**
     * #F5DEB3.
     */
     kWheat(0.9607843f, 0.87058824f, 0.7019608f),

    /**
     * #FFFFFF.
     */
     kWhite(1.0f, 1.0f, 1.0f),

    /**
     * #F5F5F5.
     */
     kWhiteSmoke(0.9607843f, 0.9607843f, 0.9607843f),

    /**
     * #FFFF00.
     */
     kYellow(1.0f, 1.0f, 0.0f),

    /**
     * #9ACD32.
     */
     kYellowGreen(0.6039216f, 0.8039216f, 0.19607843f),
    kWTF(-1,-1,-1),
    ;

    private double red;
    private double green;
    private double blue;

    private static final double kPrecision = Math.pow(2, -12);

    Color(double red, double green, double blue) {
        this.red = roundAndClamp(red);
        this.green = roundAndClamp(green);
        this.blue = roundAndClamp(blue);
    }

    private static double roundAndClamp(double value) {
        final var rounded = Math.round(value / kPrecision) * kPrecision;
        return MathUtil.clamp(rounded, 0.0, 1.0);
    }

    public double getRed() {
        return red;
    }

    public double getGreen() {
        return green;
    }

    public double getBlue() {
        return blue;
    }
}
