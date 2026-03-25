#!/bin/bash

REGION="ap-northeast-1"

# Get all instance IDs
INSTANCE_IDS=$(aws ec2 describe-instances \
  --region "$REGION" \
  --query "Reservations[].Instances[].InstanceId" \
  --output text)

echo "Instances to terminate:"
echo "$INSTANCE_IDS"

# Safety prompt
read -p "Are you sure you want to terminate ALL these instances? (yes/no): " CONFIRM

if [ "$CONFIRM" != "yes" ]; then
  echo "Aborted."
  exit 1
fi

# Terminate instances
aws ec2 terminate-instances \
  --region "$REGION" \
  --instance-ids $INSTANCE_IDS

echo "Termination command sent."