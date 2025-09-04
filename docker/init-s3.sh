#!/bin/bash

echo "🚀 LocalStack S3 초기화 시작..."

# S3 버킷 생성
awslocal s3 mb s3://test-bucket
echo "✅ S3 버킷 'test-bucket' 생성 완료"

# 버킷 목록 확인
echo "📋 현재 S3 버킷 목록:"
awslocal s3 ls

echo "🎯 LocalStack S3 초기화 완료!"