
library(foreign, pos=4)
Dataset <- read.spss("/home/sam/Downloads/ex01_078.por", 
  use.value.labels=TRUE, max.value.labels=Inf, to.data.frame=TRUE)
Dataset <- edit(as.data.frame(NULL))
library(relimp, pos=4)
showData(Dataset, placement='-20+200', font=getRcmdr('logFont'), 
  maxwidth=80, maxheight=30, suppress.X11.warnings=FALSE)
library(aplpack, pos=4)
stem.leaf(Dataset$var2, unit=1, na.rm=TRUE)
stem.leaf(Dataset$var2, style="bare", trim.outliers=FALSE, depths=FALSE, 
  reverse.negative.leaves=FALSE, na.rm=TRUE)
library(abind, pos=4)
numSummary(Dataset[,"var2"], statistics=c("mean", "sd", "quantiles"), 
  quantiles=c(0,.25,.5,.75,1))
boxplot(var2~var1, ylab="var2", xlab="var1", data=Dataset)
numSummary(Dataset[,"var2"], groups=Dataset$var1, statistics=c("mean", "sd",
   "quantiles"), quantiles=c(0,.25,.5,.75,1))
summary(Dataset)
dev.print(png, filename="/home/sam/Documents/School/STA271/hw4/RGraph.png", 
  width=500, height=500)
plot(Dataset$var2, type="h")
stem.leaf(Dataset$var2, unit=1, na.rm=TRUE)
stem.leaf(Dataset$var2, unit=1, trim.outliers=FALSE, depths=FALSE, 
  reverse.negative.leaves=FALSE, na.rm=TRUE)
stem.leaf(Dataset$var2, style="bare", unit=1, trim.outliers=FALSE, 
  depths=FALSE, reverse.negative.leaves=FALSE, na.rm=TRUE)
showData(Dataset, placement='-20+200', font=getRcmdr('logFont'), 
  maxwidth=80, maxheight=30, suppress.X11.warnings=FALSE)
write.table(Dataset, "/home/sam/Documents/School/STA271/hw4/Dataset.txt", 
  sep=",", col.names=TRUE, row.names=TRUE, quote=TRUE, na="NA")
bihai <- subset(Dataset, subset=*bihai*, select=c(var1,var2))
bihai <- subset(Dataset, subset=var1=H. bihai)
bihai <- subset(Dataset, subset=bihai)
bihai <- read.table("/home/sam/Documents/School/STA271/hw4/bihai.txt", 
  header=TRUE, sep=",", na.strings="NA", dec=".", strip.white=TRUE)
red <- read.table("/home/sam/Documents/School/STA271/hw4/red.txt", 
  header=TRUE, sep=",", na.strings="NA", dec=".", strip.white=TRUE)
yellow <- read.table("/home/sam/Documents/School/STA271/hw4/yellow.txt", 
  header=TRUE, sep=",", na.strings="NA", dec=".", strip.white=TRUE)
library(relimp, pos=4)
showData(yellow, placement='-20+200', font=getRcmdr('logFont'), maxwidth=80,
   maxheight=30, suppress.X11.warnings=FALSE)
showData(red, placement='-20+200', font=getRcmdr('logFont'), maxwidth=80, 
  maxheight=30, suppress.X11.warnings=FALSE)
fix(red)
fix(yellow)
t.test(bihai$var2, alternative='two.sided', mu=0.0, conf.level=.95)
summary(bihai)
tapply(bihai$var2, list(var1=bihai$var1), sd, na.rm=TRUE)
Dataset <- read.table("/home/sam/Documents/School/STA271/hw4/Dataset.txt", 
  header=TRUE, sep=",", na.strings="NA", dec=".", strip.white=TRUE)
library(abind, pos=4)
tapply(Dataset$var2, list(var1=Dataset$var1), sd, na.rm=TRUE)
tapply(Dataset$var2, list(var1=Dataset$var1), mean, na.rm=TRUE)
library(aplpack, pos=4)
stem.leaf(bihai$var2, style="bare", unit=1, trim.outliers=FALSE, na.rm=TRUE)
stem.leaf(red$var2, style="bare", unit=1, trim.outliers=FALSE, 
  reverse.negative.leaves=FALSE, na.rm=TRUE)
stem.leaf(yellow$var2, style="bare", unit=1, trim.outliers=FALSE, 
  na.rm=TRUE)
stem.leaf(yellow$var2, style="bare", unit=0.1, trim.outliers=FALSE, 
  na.rm=TRUE)
stem.leaf(bihai$var2, style="bare", unit=0.1, trim.outliers=FALSE, 
  na.rm=TRUE)
stem.leaf(red$var2, style="bare", unit=0.1, trim.outliers=FALSE, na.rm=TRUE)

