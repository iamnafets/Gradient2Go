aws cloudformation create-stack \
  --stack-name Gradient2Go \
  --template-url https://s3.amazonaws.com/whisper-code/CodeDeploy-West.json \
  --parameters ParameterKey=InstanceCount,ParameterValue=1 ParameterKey=InstanceType,ParameterValue=t2.micro \
    ParameterKey=KeyPairName,ParameterValue=NafPair-West ParameterKey=OperatingSystem,ParameterValue=Linux \
    ParameterKey=SSHLocation,ParameterValue=0.0.0.0/0 ParameterKey=TagKey,ParameterValue=AppName \
    ParameterKey=TagValue,ParameterValue=Gradient2Go \
  --capabilities CAPABILITY_IAM
