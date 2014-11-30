aws cloudformation create-stack \
  --stack-name Scrape2Go \
  --template-url https://s3.amazonaws.com/whisper-code/CodeDeploy-West.json \
  --parameters ParameterKey=InstanceCount,ParameterValue=2 ParameterKey=InstanceType,ParameterValue=t2.micro \
    ParameterKey=KeyPairName,ParameterValue=NafPair-West ParameterKey=OperatingSystem,ParameterValue=Linux \
    ParameterKey=SSHLocation,ParameterValue=0.0.0.0/0 ParameterKey=TagKey,ParameterValue=AppName \
    ParameterKey=TagValue,ParameterValue=Scrape2Go \
  --capabilities CAPABILITY_IAM
